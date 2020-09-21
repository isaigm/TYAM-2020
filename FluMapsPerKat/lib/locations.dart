import 'dart:io';
import 'dart:convert';
import 'package:http/http.dart' as http;
//import 'package:json_annotation/json_annotation.dart';

//part 'locations.g.dart';

//@JsonSerializable()
class LatLng {
    final double lat;
    final double lng;

    LatLng ({
        this.lat,
        this.lng
    });

    factory LatLng.fromJson (Map<String, dynamic> json) {
        return LatLng (
            lat: json ['lat'],
            lng: json ['lng'],
        );
    }

//    factory LatLng.fromJson (Map<String, dynamic> json) => _$LatLngFromJson (json);
//    Map<String, dynamic> toJson () => _$LatLngToJson (this);
}

//@JsonSerializable()
class Region {
    final LatLng coords;
    final String id;
    final String name;
    final String zoom;

    Region ({
        this.coords,
        this.id,
        this.name,
        this.zoom,
    });

    factory Region.fromJson (Map<String, dynamic> json) {
        return Region (
            coords: json ['coords'],
            id: json ['id'],
            name: json ['name'],
            zoom: json ['zoom'],
        );
    }

//    factory Region.fromJson (Map<String, dynamic> json) => _$RegionFromJson (json);
//    Map<String, dynamic> toJson () => _$RegionToJson (this);
}

//@JsonSerializable()
class Office {
    final String address;
    final String id;
    final String image;
    final double lat;
    final double lng;
    final String name;
    final String phone;
    final String region;

    Office ({
        this.address,
        this.id,
        this.image,
        this.lat,
        this.lng,
        this.name,
        this.phone,
        this.region,
    });

    factory Office.fromJson (Map<String, dynamic> json) {
        return Office (
            address: json ['address'],
            id: json ['id'],
            image: json ['image'],
            lat: json ['lat'],
            lng: json ['lng'],
            name: json ['image'],
            phone: json ['phone'],
            region: json ['region'],
        );
    }

//    factory Office.fromJson (Map<String, dynamic> json) => _$OfficeFromJson (json);
//    Map<String, dynamic> toJson () => _$OfficeToJson (this);
}

//@JsonSerializable()
class Locations {
    final List<Office> offices;
    final List<Region> regions;

    Locations ({
        this.offices,
        this.regions,
    });

    factory Locations.fromJson (Map<String, dynamic> json) {
        return Locations (
            offices: json ['offices'],
            regions: json ['regions'],
        );
    }

//    factory Locations.fromJson (Map<String, dynamic> json) => _$LocationsFromJson (json);
//    Map<String, dynamic> toJson () => _$LocationsToJson (this);
}

Future<Locations> getGoogleOffices () async {
    const googleLocationsURL = 'https://about.google/static/data/locations.json';

    final response = await http.get (googleLocationsURL);
    if (response.statusCode == 200) {
        return Locations.fromJson (json.decode (response.body));
    } else {
        throw HttpException (
          'Unexpected status code ${response.statusCode}: ${response.reasonPhrase}',
                uri: Uri.parse (googleLocationsURL)
        );
    }

}