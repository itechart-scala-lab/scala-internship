package com.itechart.internship.json

import cats.syntax.either._
import cats.syntax.functor._
import com.itechart.internship.json.Models._
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.syntax._
import monocle.Traversal

import java.time.{Instant, LocalDate, Year}

object Circe {

  /*
  JSON (JavaScript Object Notation) is an open standard file format and
  data interchange format that uses human-readable text to store and transmit data objects
  consisting of attribute–value pairs and arrays (or other serializable values).
  It is a common data format with a diverse range of functionality in data interchange
  including communication of web applications with servers.

  JSON is a language-independent data format. It was derived from JavaScript, but
  many modern programming languages include code to generate and parse JSON-format data.
  JSON filenames use the extension .json.

  Example 1:

  {
    "firstName": "John",
    "age": "33"
  }

  Example 2:

  {
    "firstName": "John",
    "lastName": "Smith",
    "isAlive": true,
    "age": 27,
    "address": {
      "streetAddress": "21 2nd Street",
      "city": "New York",
      "state": "NY",
      "postalCode": "10021-3100"
    },
    "phoneNumbers": [
      {
        "type": "home",
        "number": "212 555-1234"
      },
      {
        "type": "office",
        "number": "646 555-4567"
      }
    ],
    "children": [],
    "spouse": null
  }

  JSON is one of the most popular format for data transfer.
  Also worth to mention: XML, YAML, Binary (Avro, Parquet) etc

   */

  /*
    JSON keys are strings;
    JSON values are one of the following types:
    - Object
    - Array
    - String
    - Number
    - Boolean
    - Null
   */
  val jTrue:   Json = Json.fromBoolean(true) // or Json.True
  val jString: Json = Json.fromString("just string")
  val jNumber: Json = Json.fromInt(10)
  val jNull:   Json = Json.Null
  val jObj: Json = Json.obj(
    "boolean" -> jTrue,
    "string"  -> jString,
    "number"  -> jNumber,
    "null"    -> jNull
  )

  val jArr: Json = Json.arr(jObj, jObj)
  val result = jArr.spaces2 // jArr.noSpaces

  println(result)

  /* Exercise 1: represent raw JSON in circe types:
    {
      "title": "The Matrix",
      "year": 1999,
      "actors": ["Keanu Reeves", "Carrie-Anne Moss", "Laurence Fishburne"],
      "isRatedR" true
    }
   */

  // TODO: remove before lection
  lazy val jMatrix: Json = Json.obj(
    "title" -> Json.fromString("The Matrix"),
    "year"  -> Json.fromInt(1999),
    "actors" -> Json.arr(
      Json.fromString("Keanu Reeves"),
      Json.fromString("Carrie-Anne Moss"),
      Json.fromString("Laurence Fishburne")
    ),
    "isRatedR" -> Json.fromBoolean(true)
  )

  /* Parsing */
  val twinPeaksRawJson: String =
    """
      |{
      |  "show": "Twin Peaks",
      |  "ratings": [
      |    { "season": 1, "metaScore": 96 },
      |    { "season": 2, "metaScore": 95 },
      |    { "season": 3, "metaScore": 74 }
      |  ]
      |}""".stripMargin

  val twinPeaksParsed: Json =
    parse(twinPeaksRawJson).getOrElse(Json.Null)

  // low level API (Rarely used)
  val tpCursor: HCursor = twinPeaksParsed.hcursor
  val twinPeaksS3Score: Decoder.Result[Int] =
    tpCursor
      .downField("ratings")
      .downN(3)
      .get[Int]("metaScore")

  /* Transform */
  val oldGoodTwinPeaks: Json = tpCursor
    .downField("show")
    .withFocus(_.mapString("Old good " + _))
    .field("ratings")
    .withFocus(_.mapArray(_.init))
    .top
    .getOrElse(Json.Null)

  oldGoodTwinPeaks.spaces2

  val killersRawJson: String =
    """
      |{
      |  "artist": {
      |    "name": "The Killers",
      |    "ontour": false,
      |    "stats": {
      |      "listeners": 4517050,
      |      "playcount": 216877854
      |    },
      |    "genres": ["indie rock", "alternative rock", "new wave"],
      |    "members": [
      |      { "name": "Brandon Flowers", "instruments": ["vocals", "keyboard", "bass"] },
      |      { "name": "Dave Keuning", "instruments": ["lead guitar"] },
      |      { "name": "Mark Stoermer", "instruments": ["bass", "rhythm guitar"] },
      |      { "name": "Ronnie Vanucci Jr.", "instruments": ["drums", "percussion"] }
      |    ],
      |    "url": "https://www.last.fm/music/The+Killers"
      |  }
      |}
      |""".stripMargin
  lazy val killersOnTourJson1: Json = parse(killersRawJson)
    .getOrElse(Json.Null)
    .hcursor
    .downField("artist")
    .downField("ontour")
    .withFocus(_ => Json.True)
    .top
    .getOrElse(Json.Null)

  /* Optics */

  // Optics are a powerful tool for traversing and modifying JSON documents.
  // They can reduce boilerplate considerably, especially if you are working with deeply nested JSON.
  // circe provides support for optics by integrating with Monocle.

  object optics {
    import io.circe.optics.JsonPath._
    import monocle.Optional

    val playCountRaw: Optional[Json, Int]     = root.artist.stats.playcount.int
    val genresRaw:    Traversal[Json, String] = root.artist.members.each.string

    val parsedKillersJson: Json         = parse(killersRawJson).getOrElse(Json.Null)
    val playCount:         Option[Int]  = playCountRaw.getOption(parsedKillersJson)
    val allGenres:         List[String] = genresRaw.getAll(parsedKillersJson)

    val _oldGoodTwinPeaks: Json => Json = root.ratings.arr.modify(_.init)
    val oldGoodTwinPeaks:  Json         = _oldGoodTwinPeaks(twinPeaksParsed)

    /* Exercise 2: same as killersOnTourJson above, but using optics */

    // TODO: remove before lection
    lazy val killersOnTourJson2: Json = root.artist.ontour.boolean.modify(_ => true)(parsedKillersJson)
  }

  import optics.killersOnTourJson2

  val testOptics = killersOnTourJson2

  println()
  // -

  val intsJson:      Json                     = List(1, 2, 3).asJson
  val stringJson:    Json                     = "Crystal".asJson
  val decodedInts:   Either[Error, List[Int]] = decode[List[Int]]("[1, 2, 3]")
  val decodedString: Either[Error, String]    = decode[String]("\"Crystal\"")

  val concert: Concert = Concert(
    venue   = "London",
    date    = LocalDate.of(2009, 7, 6),
    setlist = Seq("Sam's Town", "When You Were Young")
  )

  // semiauto json encoding & decoding
  object semiauto {
    import io.circe.generic.semiauto._

//    implicit val concertDecoder: Decoder[Concert] = deriveDecoder[Concert]
//    implicit val concertEncoder: Encoder[Concert] = deriveEncoder[Concert]
    implicit val concertCodec = deriveCodec[Concert]

    val concertJson:    Json                   = concert.asJson
    val decodedConcert: Either[Error, Concert] = decode(concertJson.noSpaces)

    @JsonCodec final case class Song(title: String, lengthInSec: Int)

    val song:     Song = Song("Starman - Dawid Bowie", 253)
    val songJson: Json = song.asJson
  }

  // auto json encoding & decoding
  object auto {
    import io.circe.generic.auto._

    final case class Song(title: String, lengthInSec: Int)

    private val song = Song("Riptide - Vance Joy", 204)

    val songJson:    Json                = song.asJson
    val decodedSong: Either[Error, Song] = decode[Song](songJson.noSpaces)
  }

  // sometimes you need to do it manually (old version of circe or scala)
  object manual {
    final case class Song(title: String, length: Int)
    private val song = Song("Crystal", 249)

    implicit val songDecoder: Decoder[Song] =
      Decoder.forProduct2("title", "length")(Song.apply)
    implicit val songEncoder: Encoder[Song] =
      Encoder.forProduct2("title", "length")(s => (s.title, s.length))

    val songJson:    Json                = song.asJson
    val decodedSong: Either[Error, Song] = decode[Song](songJson.noSpaces)
  }

  // -

  object custom {
    implicit val encodeInstant: Encoder[Instant] = Encoder.encodeString.contramap[Instant](_.toString)

    implicit val decodeInstant: Decoder[Instant] = Decoder.decodeString.emap { str =>
      Either.catchNonFatal(Instant.parse(str)).leftMap(err => "Instant: " + err.getMessage)
    }

    @JsonCodec final case class TimeWindow(before: Instant, after: Instant)
    val timeWindow: TimeWindow = TimeWindow(
      before = Instant.now,
      after  = Instant.now.plusSeconds(5L)
    )
    val timeWindowJson: Json = timeWindow.asJson

    /* Writing custom codec for java.time.Year using existing one for Int */
    implicit lazy val encodeYear: Encoder[Year] =
      Encoder.encodeInt.contramap[Year](_.getValue)
    implicit lazy val decodeYear: Decoder[Year] =
      Decoder.decodeInt.emap(x => Either.catchNonFatal(Year.of(x)).leftMap(err => s"Year: ${err.getMessage}"))
  }

  object snake_case {
    import io.circe.generic.extras._

    implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

    @ConfiguredJsonCodec final case class Movie(title: String, year: Year, isRatedR: Boolean)

    val dieHard: Movie = Movie("Die Hard", Year.of(1988), isRatedR = true)

    lazy val dieHardJson: Json = dieHard.asJson // {"title":"Die Hard","year":1988,"is_rated_r":true}
  }

  object adt {
    import io.circe.generic.extras._
    import io.circe.generic.extras.semiauto._

    implicit val config: Configuration = Configuration.default
      .copy(transformConstructorNames = _.toLowerCase)

    implicit val genreCodec: Codec[Genre] = deriveEnumerationCodec[Genre]

    val hipHop: Genre = Genre.HipHop
    val hhJson: Json  = hipHop.asJson

    sealed trait Video
    final case class Movie(rating: Double) extends Video
    final case class Youtube(views: Long) extends Video

    implicit val movieDecoder: Decoder[Movie]   = deriveConfiguredDecoder[Movie]
    implicit val movieEncoder: Encoder[Movie]   = deriveConfiguredEncoder[Movie]
    implicit val ytDecoder:    Decoder[Youtube] = deriveConfiguredDecoder[Youtube]
    implicit val ytEncoder:    Encoder[Youtube] = deriveConfiguredEncoder[Youtube]

    implicit val videoDecoder: Decoder[Video] =
      List[Decoder[Video]](movieDecoder.widen, ytDecoder.widen)
        .reduceLeft(_ or _)

    implicit val videoEncoder: Encoder[Video] = Encoder.instance {
      case m:  Movie   => m.asJson
      case yt: Youtube => yt.asJson
    }

    val someSinger:     Musician     = Musician("Brandon Flowers", MusicianKind.Singer)
    val someGuitar:     Musician     = Musician("Dave Keuning", MusicianKind.Guitar)
    val someBass:       Musician     = Musician("Mark Stoermer", MusicianKind.Bass)
    val brandonFlowers: SoloMusician = SoloMusician(someSinger, Genre.Pop, gigs = Seq.empty)
    val daveKeuning:    SoloMusician = SoloMusician(someGuitar, Genre.Rock, gigs = Seq.empty)
    val markStoermer:   SoloMusician = SoloMusician(someBass, Genre.Rock, gigs = Seq.empty)

    val theKillers: Artist = Band(
      title = "The Killers",
      members = Seq(
        brandonFlowers.musician,
        daveKeuning.musician,
        markStoermer.musician,
        Musician("Ronnie Vanucci Jr.", MusicianKind.Drums)
      ),
      genre = Genre.Rock,
      gigs  = Seq(concert)
    )

    val ye: Artist = SoloMusician(
      musician = Musician("Kanye West", MusicianKind.Singer),
      genre    = Genre.HipHop,
      gigs     = Seq.empty
    )

    implicit val concertCodec:        Codec[Concert]        = deriveConfiguredCodec[Concert]
    implicit val musicianKindCodec:   Codec[MusicianKind]   = deriveEnumerationCodec[MusicianKind]
    implicit val musicianCodec:       Codec[Musician]       = deriveConfiguredCodec[Musician]
    implicit val soloMusicianDecoder: Decoder[SoloMusician] = deriveConfiguredDecoder[SoloMusician]
    implicit val soloMusicianEncoder: Encoder[SoloMusician] = deriveConfiguredEncoder[SoloMusician]
    implicit val bandDecoder:         Decoder[Band]         = deriveConfiguredDecoder[Band]
    implicit val bandEncoder:         Encoder[Band]         = deriveConfiguredEncoder[Band]

    implicit val artistDecoder: Decoder[Artist] =
      List[Decoder[Artist]](soloMusicianDecoder.widen, bandDecoder.widen)
        .reduceLeft(_ or _)

    implicit val artistEncoder: Encoder[Artist] = Encoder.instance {
      case b: Band         => b.asJson
      case s: SoloMusician => s.asJson
    }
  }

  def main(args: Array[String]): Unit = {

    import adt._

    val artists = Seq(theKillers, ye)

    val artistsJson:    Json                       = artists.asJson
    val artistsDecoded: Either[Error, Seq[Artist]] = artistsJson.as[Seq[Artist]]

    println()
  }

  // Homework

  // Create models and parse them using circe for the following APIs:
  // https://jsonplaceholder.typicode.com/posts
  // https://jsonplaceholder.typicode.com/comments
  // https://jsonplaceholder.typicode.com/albums
  // https://jsonplaceholder.typicode.com/todos
  // https://jsonplaceholder.typicode.com/users
}
