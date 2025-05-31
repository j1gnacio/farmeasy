// Función para mostrar la lista de medicamentos en el contenedor HTML
function mostrarMedicamentos(lista) {
    const contenedor = document.getElementById("catalogo-medicamentos");
    contenedor.innerHTML = ""; // Limpia contenido previo

    if (lista.length === 0) {
        contenedor.innerHTML = "<p>No se encontraron medicamentos.</p>";
        return;
    }

    lista.forEach(med => {
        const card = document.createElement("div");
        card.className = "col-md-4 mb-4";

        card.innerHTML = `
            <div class="card h-100 shadow">
                <img src="${med.imagen_url}" class="card-img-top" alt="${med.nombre}">
                <div class="card-body">
                    <h5 class="card-title">${med.nombre}</h5>
                    <p class="card-text">${med.descripcion}</p>
                    <p class="card-text fw-bold">${med.precio_internet}</p>
                    <a href="${med.url_producto}" class="btn btn-success" target="_blank" rel="noopener noreferrer">Ver producto</a>
                </div>
            </div>
        `;

        contenedor.appendChild(card);
    });
}

// Función para mapear precios y unificar campo precio_internet
function unificarPrecios(data) {
    return data.map(med => ({
        ...med,
        precio_internet: med.precio_farmacia || med.precio_internet
    }));
}

// Carga inicial: obtiene todos los medicamentos al cargar la página
fetch("/api/medicamentos")
    .then(res => {
        if (!res.ok) throw new Error("Error en la respuesta del servidor");
        return res.json();
    })
    .then(data => {
        mostrarMedicamentos(unificarPrecios(data));
    })
    .catch(err => {
        const contenedor = document.getElementById("catalogo-medicamentos");
        contenedor.innerHTML = `<p>Error al cargar medicamentos: ${err.message}</p>`;
        console.error(err);
    });

// Búsqueda en vivo llamando al backend
document.getElementById("buscador").addEventListener("input", e => {
    const texto = e.target.value.trim();

    if (texto === "") {
        fetch("/api/medicamentos")
            .then(res => res.json())
            .then(data => {
                mostrarMedicamentos(unificarPrecios(data));
            });
        return;
    }

    fetch(`/api/medicamentos/buscar?nombre=${encodeURIComponent(texto)}`)
        .then(res => {
            if (!res.ok) throw new Error("Error en la búsqueda");
            return res.json();
        })
        .then(data => {
            mostrarMedicamentos(unificarPrecios(data));
        })
        .catch(err => {
            console.error("Error al buscar medicamentos:", err);
        });
});

// Cargar farmacias únicas en el select (solo una vez al cargar la página)
fetch("/api/medicamentos/farmacias")
    .then(res => res.json())
    .then(farmacias => {
        const select = document.getElementById("filtro-farmacia");
        farmacias.forEach(nombre => {
            const option = document.createElement("option");
            option.value = nombre;
            option.textContent = nombre;
            select.appendChild(option);
        });
    });

// Filtrar medicamentos por farmacia seleccionada
document.getElementById("filtro-farmacia").addEventListener("change", e => {
    const farmacia = e.target.value;
    let url = "/api/medicamentos";

    if (farmacia !== "") {
        url += `/farmacia?nombre=${encodeURIComponent(farmacia)}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(data => mostrarMedicamentos(unificarPrecios(data)));
});
