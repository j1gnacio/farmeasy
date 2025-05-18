from playwright.sync_api import sync_playwright
from urllib.parse import urljoin
from pymongo import MongoClient

BASE_URL = "https://salcobrand.cl"

# MongoDB Atlas connection
client = MongoClient("mongodb+srv://cgallegos09:12345@farmeasyscraping.k4cs9kw.mongodb.net/?retryWrites=true&w=majority&appName=FarmEasyScraping")
db = client["farmeasyscraping"]  # nombre de tu base de datos
collection = db["medicamentos"]  # nombre de tu colección

def run():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()
        page.goto(f"{BASE_URL}/t/medicamentos")
        page.wait_for_selector("div.product.clickable")

        products = page.query_selector_all("div.product.clickable")

        for product in products:
            nombre = product.query_selector("span.product-name").inner_text().strip()
            descripcion = product.query_selector("span.product-info").inner_text().strip()
            
            precio_farmacia_element = product.query_selector("div.original-price > span.price.selling")
            precio_farmacia = precio_farmacia_element.inner_text().strip() if precio_farmacia_element else "No disponible"

            precio_internet_element = product.query_selector("div.sale-price")
            precio_internet = precio_internet_element.inner_text().strip() if precio_internet_element else "No disponible"

            imagen_url = product.query_selector("div.product-image img").get_attribute("src")

            link_relativo = product.query_selector("div.product-image a").get_attribute("href")
            url_producto = urljoin(BASE_URL, link_relativo)

            farmacia = "Salcobrand"  # nombre fijo de la farmacia

            detail_page = browser.new_page()
            detail_page.goto(url_producto)
            detail_page.wait_for_load_state('domcontentloaded')

            composicion = "No disponible"
            descripcion_breve = "No disponible"
            dosis = "No disponible"
            principio_activo = "No disponible"

            try:
                tabs = detail_page.query_selector_all("div.product-description-tab")
                for tab in tabs:
                    titulo = tab.query_selector("h2").inner_text().strip().lower()
                    if titulo == "composición":
                        composicion = tab.query_selector("div.tab-content p").inner_text().strip()
                    elif titulo == "dosis":
                        dosis = tab.query_selector("div.tab-content p").inner_text().strip()
                    elif titulo == "principio activo":
                        principio_activo = tab.query_selector("div.tab-content p").inner_text().strip()

                desc_breve_element = detail_page.query_selector("div.product-summary")
                if desc_breve_element:
                    descripcion_breve = desc_breve_element.inner_text().strip()
                else:
                    meta_desc = detail_page.query_selector("meta[name='description']")
                    if meta_desc:
                        descripcion_breve = meta_desc.get_attribute("content").strip()
            except Exception:
                pass

            medicamento = {
                "nombre": nombre,
                "descripcion": descripcion,
                "descripcion_breve": descripcion_breve,
                "dosis": dosis,
                "principio_activo": principio_activo,
                "precio_farmacia": precio_farmacia,
                "precio_internet": precio_internet,
                "imagen_url": imagen_url,
                "composicion": composicion,
                "url_producto": url_producto,
                "farmacia": farmacia
            }

            collection.insert_one(medicamento)

            print(f"Guardado: {nombre}")
            detail_page.close()

        browser.close()

if __name__ == "__main__":
    run()
