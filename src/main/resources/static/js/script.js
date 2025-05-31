fetch("/api/medicamentos")
    .then(res => res.json())
    .then(data => {
        const contenedor= document.getElementById("catalogo-medicamentos");
        data.forEach(med => {
            const card = document.createElement("div");
            card.className = "card";

            card.innerHTML = `
                <img src="${med.imagen_url || 'https://via.placeholder.com/150'}" alt="${med.nombre}">
                <h3>${med.nombre}</h3>
                <td>${med.precio_internet}</td>
                <p>${med.descripcion}</p>
                <a href="${med.url_producto}" target="_blank">Ver producto</a>
            `;
            contenedor  .appendChild(card);
        });
    })
    .catch(err => {
        alert("Error al cargar medicamentos: " + err);
    });
