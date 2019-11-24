import json
import logging
import time
import typing

import pyppeteer
from bs4 import BeautifulSoup

_logger = logging.getLogger(__name__)
from python.Product import Product

ARTICLE_SELECTOR = "article.product-tile"
COOKIES_FILE = "cookies.json"


async def get_page(url: str, wait_for="product-tile") -> str:
    # Attempt to read cookies and apply to bypass age restriction
    browser = await pyppeteer.launch()
    page = await browser.newPage()

    await page.goto(url)
    print("Downloading {}".format(url))
    await page.waitForSelector("body")

    try:
        await page.click("div.modal div.modal-buttons button")
        _logger.warning("Age restriction found! Bypassing...")
    except pyppeteer.errors.PageError:
        _logger.info("No age restricting modal found. Continuing...")

    await page.goto(url)
    await page.waitForSelector(wait_for)

    # await page.waitForSelector(wait_for, visible=True)
    data = await page.evaluate('''() => {
            return document.querySelector('body').innerHTML
        }''')

    await browser.close()
    return data


async def get_products_from_url(url, type_, subtype) -> typing.List[Product]:
    async def scrape_elements_from_url(url):
        page_html = await get_page(url)
        bs = BeautifulSoup(page_html, features="html.parser")
        time.sleep(1)
        expected_number = bs.select("div.product-category__product-count")
        all_elements = bs.select("product-tile > article.product-tile")
        # all_elements = bs.find_all("product-tile", {"model": "tile"})
        # all_elements = list(map(lambda e: e.find("article", class_="product-tile"), all_elements))
        if len(all_elements) == 0:
            print("Fan dåeh!")
        return all_elements

    elements = await scrape_elements_from_url(url)

    products = []
    for el in elements:
        products.append(Product(el, type_, subtype))

    return products
