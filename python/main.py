import asyncio
import json
import logging
import time

import click
import pyppeteer

from python.Product import Product
from python.scraper import get_products_from_url
from python.urls import get_sub_pages, BASE_URL

_logger = logging.getLogger(__name__)
sub_pages = None


async def get_all_products():
    sub_pages = await get_sub_pages()
    all_products = []
    failed_pages = []
    for sub in sub_pages:
        url = "{}{}".format(BASE_URL, sub["url"])
        products = await get_products_from_url(url, type_=sub["type"], subtype=sub["subtype"])
        _logger.info("Found %d products from %s.", len(products), sub)

        if len(products) == 0:
            failed_pages.append(sub)
        all_products.extend(products)

    for fail in failed_pages:
        _logger.warning("Failed to find products for %s.", fail)
    return all_products


def convert_to_json(all_json, objs):
    _logger.info("Converting %d products to JSON.", len(objs))
    amount = len(objs)
    start = time.time_ns()
    for i, o in enumerate(objs):
        all_json.append(o.to_json())
        end = time.time_ns()
        _logger.info("Finished converting product %d/%d to JSON. Elapsed time: %s ms.", i, amount, (end - start) // 1000 ** 2)

    end = time.time_ns()
    _logger.info("Finished converting %d products in total time: %s s", len(objs), (end - start) // 1000 ** 3)


def write_all_products_to_json(path="./db.json"):
    try:
        all_products = asyncio.get_event_loop().run_until_complete(get_all_products())
    except (pyppeteer.errors.NetworkError, pyppeteer.errors.PageError) as e:
        raise e

    all_json = []

    try:
        convert_to_json(all_json, all_products)
    except (pyppeteer.errors.NetworkError, pyppeteer.errors.PageError) as e:
        raise e

    with open(path, "w") as file:
        json.dump(all_json, file)


def load_all_products_from_json(path="./db.json"):
    with open(path, 'r') as file:
        db = json.load(file)

    products = []
    for p in db:
        products.append(Product.load_from_json(p))

    products = list(sorted(db, key=lambda p: p["price"]))
    for p in products:
        print("{}: {}".format(p["name"], p["abv"] / p["price"]))


@click.command()
@click.option("--log-level", default=logging.DEBUG, help="Level of logging to output with 'logger' module.")
def main(log_level):
    logging.root.setLevel(log_level)

    # create console handler and set level to debug
    ch = logging.StreamHandler()
    ch.setLevel(log_level)

    # create formatter
    formatter = logging.Formatter('%(asctime)s [%(levelname)s] %(name)s: %(message)s')

    # add formatter to ch
    ch.setFormatter(formatter)

    # add ch to logger
    _logger.addHandler(ch)
    write_all_products_to_json()


if __name__ == '__main__':
    main()
