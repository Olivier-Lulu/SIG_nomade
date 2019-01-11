//fonction pour recuperer de maniere synchrone le resultat d'un get
function httpGet(theUrl)
{
    const xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

//definition du style des batiment et parking
function batimentStyle(feature, resolution) {
    const properties = feature.getProperties();

    if(properties.type === 'batiment' || properties.type === 'salleSport' ||
       properties.type === 'hopital' || properties.type === 'cabinetMedical'){
        //si batiment en gris
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(45, 45, 45, 0.8)'
            })
        });

    }else {
        if (properties.type === 'parking') {
            //si c'est un parking en gris clair
            return new ol.style.Style({
                fill: new ol.style.Fill({
                    color: 'rgba(46, 44, 62, 0.4)'
                })
            });

        } else {
            if(properties.type === 'parcoursSante'){
                return new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: 'rgba(103, 124, 48, 0.6)'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(103, 124, 48, 0.6)'
                    })
                });
            }
        }
    }

    //si c'est inconnue en rouge
    return new ol.style.Style({
        fill: new ol.style.Fill({
            color: 'rgba(255, 0, 0, 0.8)'
        })
    });
}

//definition du style des rues
function voieStyle(feature, resolution) {
    const properties = feature.getProperties();
    if(properties.name == null){
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(163, 125, 48, 0.6)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(163, 125, 48, 0.6)'
            })
        });

    }else {
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(163, 125, 48, 0.6)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(163, 125, 48, 0.6)'
            })
        });

    }
}

//definition du style des elements naturels
function natureStyle(feature, resolution) {
    const properties = feature.getProperties();

    if (properties.type === 'basin' || properties.type === 'water' || properties.type === 'river') {
        //si c'est de l'eau en bleu
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(0, 0, 255, 0.7)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0, 0, 255, 0.7)'
            })
        });

    }else{
        if(properties.type === 'grass' || properties.type === 'scrub' || properties.type === 'meadow'){
            //si c'est de l'herbe en vert clair
            return new ol.style.Style({
                fill: new ol.style.Fill({
                    color: 'rgba(103, 124, 48, 0.6)'
                }),
                stroke: new ol.style.Stroke({
                    color: 'rgba(103, 124, 48, 0.6)'
                })
            });

        }else{
            if(properties.type === 'wood' || properties.type === 'forest'){
                //si c'est de la foret en vert fonce
                return new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: 'rgba(0, 76, 0, 0.7)'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(0, 76, 0, 0.7)'
                    })
                });

            }else{
                if(properties.type === 'brownfield'){
                    //si c'est un friche en orange
                    return new ol.style.Style({
                        fill: new ol.style.Fill({
                            color: 'rgba(170, 142, 0, 0.7)'
                        }),
                        stroke: new ol.style.Stroke({
                            color: 'rgba(170, 142, 0, 0.7)'
                        })
                    });

                }else{
                    //sinon on panique et on affiche ce qui a pas de style dans la console
                    console.log(properties);
                }
            }
        }
    }
}

//fonction pour recuperer un layer de type vector a partir des capabilities
function getVectorLayer(capabilities,layerName, style) {
    const wmtsOption = ol.source.WMTS.optionsFromCapabilities(capabilities,{
        layer: layerName,
        matrixSet: 'EPSG:4326',
        format: 'application/vnd.mapbox-vector-tile'
    });
    const src = new ol.source.WMTS(wmtsOption);

    return new ol.layer.VectorTile({
        source: new ol.source.VectorTile({
            tileUrlFunction: src.getTileUrlFunction(),
            tileGrid: src.getTileGrid(),
            format: new ol.format.MVT()
        }),
        style: style,
        extent : [1.92377,47.83913,1.94151,47.84943]
    });
}

function markerStyle (feature, resolution){
    const properties = feature.getProperties();

    return new ol.style.Style({
        image: new ol.style.Circle({
            radius: 7,
            fill: new ol.style.Fill({color: 'black'}),
            stroke: new ol.style.Stroke({
                color: 'white', width: 2
            })
        })
    });
};


function toRad(x) { return x * Math.PI / 180; }

//formule de distance entre deux point en longitude/latitude vers des metre par la formule de Haversine
//https://www.movable-type.co.uk/scripts/latlong.html
function distanceBetween(pointA, pointB){
    var R = 6371e3; // metres
    var phi1 = toRad(pointA[1]);
    var phi2 = toRad(pointB[1]);
    var deltaPhi = toRad(pointB[1]-pointA[1]);
    var deltaLambda = toRad(pointA[0]-pointB[0]);

    var a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
        Math.cos(phi1) * Math.cos(phi2) *
        Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    return R * c;
}

function showMarkerPopup (content, overlay, evt, tags)
{
    var tagsJson = JSON.parse(tags);

    var selectHtml = '<select id="markerTag" size="10">';
    for (index in tagsJson) {
          selectHtml += '<option value="' + tagsJson[index] + '">' + tagsJson[index] + '</option>';
    }
    selectHtml += '</select>';

    content.innerHTML = 'Nom : <input type="text" id="markerNom" size="10"><br>'
        + 'Tag : ' + selectHtml + '<br>'
        + '<button onClick="enregistrerMarker()">Enregistrer</button>';

    overlay.setPosition(evt.coordinate);
}

//la fonction enregistrerMarker et deleteMarker a besoin du contexte d'execution de la page pour
//fonctionner donc si on aurait la laisser dans ce fichier, elle est dans index.html pour avoir le
//contexte sous les yeux

function search (searchLayer)
{
    const critere = Android.getCritere();
    if(critere) {
        const temp = Android.getLocations();
        if(temp) {
            const points = JSON.parse(temp);

            if (searchLayer) {
                var marker;
                var keyIndex = 0;
                for (marker in points) {
                    marker = new ol.Feature({
                        name : points[marker].nom,
                        type: 'searchedMarker',
                        geometry: new ol.geom.Point([points[marker].lon, points[marker].lat]),
                        id : Object.keys(points)[keyIndex]
                    });
                    keyIndex++;

                    searchLayer.addFeature(marker);
                }
            }
        }
    }
}