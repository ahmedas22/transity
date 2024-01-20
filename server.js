const express = require("express");
const axios = require("axios");
const app = express();
const port = 3000;
const fs = require("fs");
const converter = require("json-2-csv");
const routes = [
  10,
  11,
  12,
  14,
  15,
  16,
  17,
  18,
  19,
  20,
  21,
  22,
  23,
  24,
  25,
  26,
  28,
  29,
  30,
  31,
  32,
  33,
  34,
  35,
  36,
  38,
  40,
  41,
  42,
  43,
  44,
  45,
  46,
  47,
  48,
  49,
  50,
  53,
  54,
  55,
  56,
  57,
  58,
  59,
  60,
  65,
  66,
  67,
  68,
  71,
  74,
  75,
  77,
  78,
  79,
  82,
  83,
  85,
  87,
  88,
  89,
  90,
  91,
  92,
  93,
  95,
  96,
  97,
  98,
  635,
  641,
  642,
  649,
  650,
  662,
  671,
  672,
  676,
  677,
  690,
  691,
  693,
  694,
  "BLUE",
];
let result = [];

app.get("/", (req, res) => {
  res.send("Hello World!");
});

app.get("/getData", async (req, res) => {
  try {
    result = [];
    for (const item of routes) {
      const res1 = await axios.get(
        `https://api.winnipegtransit.com/v3/stops.json?api-key=337kgozzwpTUnFW7BvqE&route=${item}`
      );
      for (const stop of res1.data.stops) {
        result.push({
          route: item,
          key: stop.key,
          name: stop.name,
          direction: stop.direction,
          latitude: stop.centre.geographic.latitude,
          longitude: stop.centre.geographic.longitude,
        });
      }
    }

    res.json(result);
  } catch (error) {
    console.error(error);
  }
});

app.get("/getPassupLocation", async (req, res) => {
  try {
    result = [];
    const res1 = await axios.get(
      "https://data.winnipeg.ca/resource/mer2-irmb.json?$where=time>='2019-05-01T00:00:00.000'&$limit=1000000"
    );
    const res2 = JSON.parse(fs.readFileSync("stops.json"));
    for (const passups of res1.data) {
      let minJ = 0;
      if (
        passups.location !== undefined &&
        passups.location.coordinates !== undefined
      ) {
        for (let j = 0; j < res2.length; j++) {
          if (
            Math.abs(passups.location.coordinates[1] - res2[j].latitude) <=
              Math.abs(passups.location.coordinates[1] - res2[minJ].latitude) &&
            Math.abs(passups.location.coordinates[0] - res2[j].longitude) <=
              Math.abs(passups.location.coordinates[1] - res2[minJ].latitude)
          ) {
            minJ = j;
          }
        }
        result.push({
          passUpId: passups.pass_up_id,
          stopID: res2[minJ].key,
        });
      }
    }
    let data = JSON.stringify(result);
    fs.writeFileSync("PassupLocation.json", data);
    res.json(result);
  } catch (error) {
    console.error(error);
  }
});

app.get("/getPassups", async (req, res) => {
  try {
    result = [];
    const res1 = await axios.get(
      "https://data.winnipeg.ca/resource/mer2-irmb.json?$where=time>='2019-05-01T00:00:00.000'&$limit=1000000"
    );
    let timeAndYear;
    let date;
    for (const passups of res1.data) {
      if (
        passups.location !== undefined &&
        passups.location.coordinates !== undefined
      ) {
        timeAndYear = passups.time.split("T");
        date = timeAndYear[0].split("-");
        result.push({
          passUpId: passups.pass_up_id,
          type: passups.pass_up_type,
          time: timeAndYear[1],
          year: date[0],
          month: date[1],
          day: date[2],
          route: passups.route_number,
          destination: passups.route_destination,
          latitude: passups.location.coordinates[0],
          longitude: passups.location.coordinates[1],
        });
      }
    }
    let data = JSON.stringify(result);
    fs.writeFileSync("Passups.json", data);
    res.json(result);
  } catch (error) {
    console.error(error);
  }
});

app.get("/getOn", async (req, res) => {
  try {
    result = [];
    const res1 = await axios.get(
      "https://data.winnipeg.ca/resource/gp3k-am4u.json?$where=scheduled_time>='2019-05-01T00:00:00.000'&$limit=1000000"
    );
    let timeAndYear;
    let date;
    for (const ons of res1.data) {
      console.log(ons.row_id);
      timeAndYear = ons.scheduled_time.split("T");
      date = timeAndYear[0].split("-");
      result.push({
        onID: ons.row_id,
        type: ons.pass_up_type,
        time: timeAndYear[1],
        year: date[0],
        month: date[1],
        day: date[2],
        route: ons.route_number,
        destination: ons.route_destination,
        dayType: ons.day_type,
        deviation: ons.deviation,
        latitude: ons.location.coordinates[0],
        longitude: ons.location.coordinates[1],
      });
    }
    converter.json2csv(result, (err, csv) => {
      if (err) {
        throw err;
      }
      // write CSV to a file
      fs.writeFileSync("On.csv", csv);
    });
    res.send("hi");
  } catch (error) {
    console.error(error);
  }
});

app.get("/getOnLocation", async (req, res) => {
  try {
    result = [];
    const res1 = await axios.get(
      "https://data.winnipeg.ca/resource/gp3k-am4u.json?$where=scheduled_time>='2019-05-01T00:00:00.000'&$limit=1000000"
    );
    for (const ons of res1.data) {
      result.push({
        onID: ons.row_id,
        stopID: ons.stop_number,
      });
    }
    let data = JSON.stringify(result);
    fs.writeFileSync("OnTimeLocation.json", data);
    res.send("result");
  } catch (error) {
    console.error(error);
  }
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});
