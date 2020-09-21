import 'package:FluMapsPerKat/locations.dart';
import 'package:flutter/material.dart';
import 'MapSample.dart';

void main () => runApp (MyApp ());

class MyApp extends StatelessWidget {

  final appTitle = "Google Maps from Flutter";

  @override
  Widget build (BuildContext context) {
    return MaterialApp (
      title: appTitle,
      home: PKAT (),
    );
  }

}

class PKAT extends StatelessWidget {

  @override
  Widget build (BuildContext context) {
    return Scaffold (
      appBar: AppBar (title: Text ("Google Maps App")),
      body: Center (child: MapSample ()),
      drawer: Drawer (
        child: ListView (
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader (
              child: Text ('Drawer Header PKAT'),
              decoration: BoxDecoration (color: Colors.blue),
            ),
            ListTile (
              title: Text ('Item 1'),
              onTap: () {
                Navigator.pop (context);
              },
            ),
            ListTile (
              title: Text ('Item 2'),
              onTap: () {
                Navigator.pop (context);
              },
            ),
          ],
        ),
      ),
    );
  }

}




