import re

import requests
from bs4 import BeautifulSoup

from python.global_constants import BASE_URL

# ---- Selectors for elements ----
PRICE_AND_VOLUME_SELECTOR = "div.product-tile__description span"
NAME_SELECTOR = "div.product-tile__title-container a h2 pre"
LINK_SELECTOR = "div.product-tile__title-container a"

REG_NAME_AND_ABV = re.compile(r"(.+)\s([\d,]+)%", flags=re.I | re.U)
REG_ABV = re.compile(r"\d+,?\d*\s?%", flags=re.I | re.U)
REG_VOLUME_UNIT = re.compile(r"([\d×\s,]+)\s?(\w+)", flags=re.I | re.U)
REG_VOLUME_PROD = re.compile(r"(\d+)\s?×\s?(\d+)")
REG_PRICE_AND_CURRENCY = re.compile(r".+:\s([\d,]+)\s?(\w+)")

UNIT_TO_ML = {
    "cl": 10,
    "ml": 1,
    "dl": 100,
    "l": 1000
}


class Product:

    def __init__(self, prod_obj, type_, subtype):
        self.type = type_
        self.subtype = subtype
        self.obj = prod_obj
        self.name = None
        self.price = None
        self.abv = None
        self.volume = None
        self.currency = None
        self.product_link = None

    @classmethod
    def load_from_json(cls, json_data):
        cls.type = json_data["type"]
        #cls.subtype = json_data["subtype"]
        cls.name = json_data["name"]
        cls.price = json_data["price"]
        cls.abv = json_data["abv"]
        cls.volume = json_data["volume"]
        cls.currency = json_data["currency"]
        cls.product_link = json_data["product_link"]
        return cls

    def get_product_link(self) -> str:
        if self.product_link is not None:
            return self.product_link

        self.product_link = self.obj.select(LINK_SELECTOR)[0].get("href")
        return self.product_link

    def get_subtype(self) -> str:
        return self.subtype

    def get_type(self) -> str:
        return self.type

    def get_abv(self) -> float:
        """
        Get the alcohol-by-volume in percent.
        :return: the alcohol-by-volume in percent.
        """
        if self.abv is not None:
            return self.abv

        self.get_name()
        return self.abv

    def get_name(self) -> str:
        if self.name is not None:
            return self.name

        name_and_abv = self.obj.select(NAME_SELECTOR)[0].text
        name_and_abv = name_and_abv.replace("\n", "")
        match = re.match(REG_NAME_AND_ABV, name_and_abv)

        if match:
            self.name = match.group(1).strip()
            self.abv = float(match.group(2).strip().replace(",", "."))
        else:
            self.name = name_and_abv.strip()
            self._follow_product_link_and_get_abv()

        return self.name

    def get_volume(self) -> int:
        """
        Gets the total volume of this product in milliliters.
        :return: the volume of this product in milliliters.
        """
        if self.volume is not None:
            return self.volume

        volume_and_unit = self.obj.select(PRICE_AND_VOLUME_SELECTOR)[0].text
        match = re.match(REG_VOLUME_UNIT, volume_and_unit)
        unit = match.group(2).strip().lower()
        unit_mult = UNIT_TO_ML[unit]
        match_vol = re.match(REG_VOLUME_PROD, match.group(1))
        volume = int(match_vol.group(1).strip()) * float(match_vol.group(2).strip().replace(",", ".")) if match_vol else float(match.group(
            1).replace(",", "."))
        self.volume = volume * unit_mult
        return self.volume

    def get_currency(self) -> str:
        if self.currency is not None:
            return self.currency

        self.get_price()
        return self.currency

    def get_price(self) -> float:
        if self.price is not None:
            return self.price

        price_and_currency = self.obj.select(PRICE_AND_VOLUME_SELECTOR)[1].text
        match = re.match(REG_PRICE_AND_CURRENCY, price_and_currency)
        self.price = float(match.group(1).strip().replace(",", "."))
        self.currency = match.group(2)

        return self.price

    def to_json(self) -> dict:
        return {
            "type": self.get_type(),
            "subtype": self.get_subtype(),
            "name": self.get_name(),
            "abv": self.get_abv(),
            "price": self.get_price(),
            "currency": self.get_currency(),
            "volume": self.get_volume(),
            "product_link": self.get_product_link()
        }

    def _follow_product_link_and_get_abv(self):
        url = "{}{}".format(BASE_URL, self.get_product_link())
        res = requests.get(url)
        bs = BeautifulSoup(res.text, features="html.parser")
        abv_candidates = bs.select("div.product-details__section > dl > dd")
        for cand in abv_candidates:
            text = cand.text
            if re.match(REG_ABV, text):
                self.abv = float(text[:-1].replace(",", ".").strip())
                break
