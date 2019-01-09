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

    if(properties.name == null){
        //si ca a pas de nom en gris
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(45, 45, 45, 0.8)'
            })
        });
    }else {
        if (properties.name === 'parking') {
            //si c'est un parking en gris clair
            return new ol.style.Style({
                fill: new ol.style.Fill({
                    color: 'rgba(46, 44, 62, 0.4)'
                })
            });
        } else {
            //si ca a un nom en gris
            return new ol.style.Style({
                fill: new ol.style.Fill({
                    color: 'rgba(45, 45, 45, 0.8)'
                })
            });
        }
    }
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
        //TODO difference entre voie, avenue, rue ...
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

//definition du style des machin naturel
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
                //si c'est de la foret en vert foncer
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

//definition du style des rues
function eqmStyle(feature, resolution) {
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(0, 0, 0, 1)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0, 0, 0, 1)'
            })
        });
}

//fonction pour recuperer un layer de type vector a partirs des capabilities
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
            style: style
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

//formule de distance entre deux point en longitude/latitude vers des metre par la formule de haversine
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

function showMarkerPopup (content, overlay, evt, marker)
{
    content.innerHTML = 'Nom : <input type="text" id="markerNom" size="10"><br>'
                        + 'Tag : <input type="text" id="markerTag" size="10"><br>'
                        + '<button onClick="enregistrerMarker()">Enregistrer</button>';
    //la fonction enregistrerMarker a besoin du contexte d'execution de la page pour fonctionner
    //donc si on aurait la laisser dans ce fichier, elle est dans index.html pour avoir le contexte
    //sous les yeux
    overlay.setPosition(evt.coordinate);
}

function goTo(){ console.log(target); }

