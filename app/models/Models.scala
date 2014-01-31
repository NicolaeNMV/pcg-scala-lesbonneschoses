package models

import io.prismic._
import controllers._

case class RichStructuredText(st: Fragment.StructuredText) {
  def text: Option[String] = {
    Some(st.blocks.collect { case b: Fragment.StructuredText.Block.Text => b.text }.mkString("\n")).filterNot(_.isEmpty)
  }
  def html(linkResolver: DocumentLinkResolver): Option[String] = {
    Some(st.asHtml(linkResolver))
  }
}

case class RichColor(color: Fragment.Color) {
  def text: Option[String] = {
    Some(color.hex)
  }
  def html: Option[String] = {
    Some(color.asHtml)
  }
}

object PcgImplicits {
  implicit def toRichStructuredText(st: Fragment.StructuredText): RichStructuredText = {
    new RichStructuredText(st)
  }

  implicit def toRichStructuredTextOpt(st: Option[Fragment.StructuredText]): Option[RichStructuredText] = {
    st.map(new RichStructuredText(_))
  }

  implicit def toRichColor(color: Fragment.Color): RichColor = {
    new RichColor(color)
  }

  implicit def toRichColorOpt(color: Option[Fragment.Color]): Option[RichColor] = {
    color.map(new RichColor(_))
  }
}


class Product(val document: io.prismic.Document)(implicit ctx: Prismic.Context) {
  import PcgImplicits._

  val maskName = "product"
  def id: String = document.id
  def slug: String = document.slug
  def tags: Seq[String] = document.tags
  def name: Option[RichStructuredText] = document.getStructuredText(s"$maskName.name")
  def short_lede: Option[RichStructuredText] = document.getStructuredText(s"$maskName.short_lede")
  def flavour: Option[RichStructuredText] = document.getStructuredText(s"$maskName.flavour")
  def color: Option[RichColor] = document.getColor(s"$maskName.color")
  def description: Option[RichStructuredText] = document.getStructuredText(s"$maskName.description")
  def image: Option[Fragment.Image] = document.getImage(s"$maskName.image")
  def image_main: Option[Fragment.Image.View] = document.getImage(s"$maskName.image", "main")
  def image_icon: Option[Fragment.Image.View] = document.getImage(s"$maskName.image", "icon")
  def gallery_main: Option[Fragment.Image.View] = document.getImage(s"$maskName.gallery", "main")
  def price: Option[Fragment.Number] = document.getNumber(s"$maskName.price")
  def price(pattern: String): Option[String] = document.getNumber(s"$maskName.price", pattern)
  def testimonial_author: Option[RichStructuredText] = document.getStructuredText(s"$maskName.testimonial_author")
  def testimonial_quote: Option[RichStructuredText] = document.getStructuredText(s"$maskName.testimonial_quote")
  //Select => getText()
}






/*
{
  "Main information" : {
    "name" : {
      "type" : "StructuredText",
      "fieldset" : "Product name",
      "config" : {
        "single" : "heading1,em,strong",
        "placeholder" : "The product's name"
      }
    },
    "short_lede" : {
      "type" : "StructuredText",
      "fieldset" : "Short catcher",
      "config" : {
        "single" : "heading2,em,strong",
        "placeholder" : "Shortest catcher to the product (about 15 words)"
      }
    },
    "description" : {
      "type" : "StructuredText",
      "fieldset" : "Description",
      "config" : {
        "multi" : "paragraph,em,strong,hyperlink",
        "placeholder" : "Exhaustive coverage of the product",
        "minHeight" : "100px"
      }
    },
    "image" : {
      "type" : "Image",
      "config" : {
        "constraint" : {
          "width" : 500,
          "height" : 500
        },
        "thumbnails" : [ {
          "name" : "Icon",
          "width" : 250,
          "height" : 250
        } ]
      }
    }
  },
  "Additional informations" : {
    "related[0]" : {
      "fieldset" : "Related products",
      "type" : "Link",
      "config" : {
        "placeholder" : "Related product #1",
        "select" : "document",
        "masks" : [ "product" ]
      }
    },
    "related[1]" : {
      "type" : "Link",
      "config" : {
        "placeholder" : "Related product #2",
        "select" : "document",
        "masks" : [ "product" ]
      }
    },
    "related[2]" : {
      "type" : "Link",
      "config" : {
        "placeholder" : "Related product #3",
        "select" : "document",
        "masks" : [ "product" ]
      }
    },
    "allergens" : {
      "type" : "Text",
      "fieldset" : "Product properties",
      "config" : {
        "label" : "Allergens"
      }
    },
    "price" : {
      "type" : "Number",
      "config" : {
        "label" : "Price (in $)",
        "min" : 0
      }
    },
    "flavour[0]" : {
      "type" : "Select",
      "config" : {
        "label" : "Flavour",
        "options" : [ "Chocolate", "Lemon/lime", "Berries", "Pistachio", "Soft fruit", "Vanilla", "Caramel" ]
      }
    },
    "flavour[1]" : {
      "type" : "Select",
      "config" : {
        "label" : "Secondary flavour",
        "options" : [ "Chocolate", "Lemon/lime", "Berries", "Pistachio", "Soft fruit", "Vanilla", "Caramel", "Spice" ]
      }
    },
    "color" : {
      "type" : "Color",
      "config" : {
        "label" : "Color tag"
      }
    }
  },
  "Gallery" : {
    "gallery[0]" : {
      "fieldset" : "Image gallery",
      "type" : "Image",
      "config" : {
        "constraint" : {
          "width" : 1500,
          "height" : 500
        },
        "thumbnails" : [ {
          "name" : "Squared",
          "width" : 500,
          "height" : 500
        }, {
          "name" : "Small",
          "width" : 150,
          "height" : 50
        } ]
      }
    },
    "gallery[1]" : {
      "type" : "Image",
      "config" : {
        "constraint" : {
          "width" : 1500,
          "height" : 500
        },
        "thumbnails" : [ {
          "name" : "Medium",
          "width" : 750,
          "height" : 250
        }, {
          "name" : "Small",
          "width" : 150,
          "height" : 50
        } ]
      }
    },
    "gallery[2]" : {
      "type" : "Image",
      "config" : {
        "constraint" : {
          "width" : 1500,
          "height" : 500
        },
        "thumbnails" : [ {
          "name" : "Medium",
          "width" : 750,
          "height" : 250
        }, {
          "name" : "Small",
          "width" : 150,
          "height" : 50
        } ]
      }
    }
  },
  "Reviews" : {
    "testimonial_author[0]" : {
      "fieldset" : "Gourmets reviews",
      "type" : "StructuredText",
      "config" : {
        "single" : "heading3",
        "placeholder" : "First author"
      }
    },
    "testimonial_quote[0]" : {
      "type" : "StructuredText",
      "config" : {
        "single" : "p,strong,em",
        "minHeight" : "50px",
        "placeholder" : "First quote"
      }
    },
    "testimonial_author[1]" : {
      "type" : "StructuredText",
      "config" : {
        "single" : "heading3",
        "placeholder" : "Second author"
      }
    },
    "testimonial_quote[1]" : {
      "type" : "StructuredText",
      "config" : {
        "single" : "p,strong,em",
        "minHeight" : "50px",
        "placeholder" : "Second quote"
      }
    }
  }
}
 */