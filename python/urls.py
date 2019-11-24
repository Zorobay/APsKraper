# ---- url constants ----
import typing

from bs4 import BeautifulSoup

from python.global_constants import BASE_URL
from python.scraper import get_page


async def get_sub_pages() -> typing.List[dict]:
    page = await get_page(BASE_URL, wait_for="body")
    bs = BeautifulSoup(page, features="html.parser")
    menu_items = bs.select("div.main-container > header > nav.menu > div > ul > li")
    wanted_items = [menu_items[i] for i in [1, 4, 5, 6]]

    output = []
    for item in wanted_items:
        superclass = item.find("a").text.strip()
        sub_items = item.select("li.__submenu-list-item > a")

        for sub in sub_items:
            url = sub["href"]
            subclass = sub.text.strip()
            output.append({
                "type": superclass,
                "subtype": subclass,
                "url": url
            })
    return output