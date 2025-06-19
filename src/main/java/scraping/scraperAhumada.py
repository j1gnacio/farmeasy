from playwright.sync_api import sync_playwright
from urllib.parse import urljoin
from pymongo import MongoClient
import time

BASE_URL = "https://www.farmaciasahumada.cl"

client = MongoClient("mongodb+srv://cgallegos09:12345@farmeasyscraping.k4cs9kw.mongodb.net/?retryWrites=true&w=majority&appName=FarmEasyScraping")
db = client["farmeasyscraping"]
collection = db["medicamentos"]

def run():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()
        page.goto(f"{BASE_URL}/medicamentos")
        page.wait_for_selector("div.product.product-tile-wrapper")

        # Cargar todos los medicamentos pulsando "Cargar más"
        while True:
            try:
                cargar_mas_btn = page.query_selector('button.load-more')
                if not cargar_mas_btn or not cargar_mas_btn.is_enabled():
                    break
                cargar_mas_btn.click()
                time.sleep(3)
            except:
                break

        products = page.query_selector_all("div.product.product-tile-wrapper")

        for product in products:
            try:
                nombre = product.query_selector("div.pdp-link a.link").inner_text().strip()
            except:
                nombre = "No disponible"

            try:
                marca = product.query_selector("div.product-tile-brand span.link").inner_text().strip()
            except:
                marca = "No disponible"

            try:
                precio_element = product.query_selector("div.price span.value")
                precio_farmacia = precio_element.inner_text().strip() if precio_element else "No disponible"
            except:
                precio_farmacia = "No disponible"

            try:
                link_relativo = product.query_selector("div.image-container a").get_attribute("href")
                url_producto = urljoin(BASE_URL, link_relativo)
            except:
                url_producto = ""

            try:
                imagen_url = product.query_selector("div.image-container img.tile-image").get_attribute("src")
            except:
                imagen_url = "No disponible"

            farmacia = "Farmacias Ahumada"

            # Abrir página detalle para extraer datos extra si es posible
            composicion = "No disponible"
            descripcion_breve = "No disponible"
            dosis = "No disponible"
            principio_activo = "No disponible"

            if url_producto:
                detail_page = browser.new_page()
                detail_page.goto(url_producto)
                detail_page.wait_for_load_state('domcontentloaded')

                try:
                    # Extraemos tabs si existen
                    tabs = detail_page.query_selector_all("div.product-description-tab")
                    for tab in tabs:
                        titulo = tab.query_selector("h2").inner_text().strip().lower()
                        if titulo == "composición":
                            composicion = tab.query_selector("div.tab-content p").inner_text().strip()
                        elif titulo == "dosis":
                            dosis = tab.query_selector("div.tab-content p").inner_text().strip()
                        elif titulo == "principio activo":
                            principio_activo = tab.query_selector("div.tab-content p").inner_text().strip()
                except:
                    pass

                try:
                    desc_breve_element = detail_page.query_selector("div.product-summary")
                    if desc_breve_element:
                        descripcion_breve = desc_breve_element.inner_text().strip()
                    else:
                        meta_desc = detail_page.query_selector("meta[name='description']")
                        if meta_desc:
                            descripcion_breve = meta_desc.get_attribute("content").strip()
                except:
                    pass

                detail_page.close()

            medicamento = {
                "nombre": nombre,
                "marca": marca,
                "descripcion": descripcion_breve,  # Por ahora descripción breve en 'descripcion'
                "descripcion_breve": descripcion_breve,
                "dosis": dosis,
                "principio_activo": principio_activo,
                "precio_farmacia": precio_farmacia,
                "precio_internet": "No disponible",  # No encontré precio internet diferenciado aquí
                "imagen_url": imagen_url,
                "composicion": composicion,
                "url_producto": url_producto,
                "farmacia": farmacia
            }

            collection.update_one(
                {"nombre": nombre, "farmacia": farmacia},
                {"$set": medicamento},
                upsert=True
            )

            print(f"Guardado: {nombre}")

        browser.close()
        client.close()

if __name__ == "__main__":
    run()
