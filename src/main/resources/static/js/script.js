fetch("/api/medicamentos")
    .then(res => res.json())
    .then(data => {
        const tabla = document.getElementById("tabla-medicamentos");
        data.forEach(med => {
            const fila = document.createElement("tr");

            fila.innerHTML = `
                <td>${med.nombre}</td>
                <td>$${med.precio_internet}</td>
                <td>${med.descripcion}</td>
                <td><a href="${med.url_producto}" target="_blank">Ver producto</a></td>
            `;
            tabla.appendChild(fila);
        });
    })
    .catch(err => {
        alert("Error al cargar medicamentos: " + err);
    });
