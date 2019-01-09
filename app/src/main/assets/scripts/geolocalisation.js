//test si objet est dans array
function contains(objet, array)
{
    return (array.indexOf(objet) > -1);
}

//return true si featA est plus proche de pos
//return false si featB est plus proche de pos
function plusProche(pos,featA, featB){
    const pA = featA.getGeometry().getClosestPoint(pos);
    const pB = featB.getGeometry().getClosestPoint(pos);
    const distanceA = (pA[0]-pos[0])*(pA[0]-pos[0])+(pA[1]-pos[1])*(pA[1]-pos[1]);
    const distanceB = (pB[0]-pos[0])*(pB[0]-pos[0])+(pB[1]-pos[1])*(pB[1]-pos[1]);
    if(distanceA < distanceB){
        return true;
    }else{
        return false;
    }
}

function addGeoloc(map){
    //recupere les coordonée de l'utilisateur
    const geolocation = new ol.Geolocation({
        // enableHighAccuracy must be set to true to have the heading value.
        tracking: true,
        trackingOptions: {
            enableHighAccuracy: true
        },
        projection: map.getView().getProjection()
    });

    //gestion des erreur de geolocation
    geolocation.on('error', function(error) {
        const info = document.getElementById('err');
        info.innerHTML = error.message;
        info.style.display = '';
    });

    //met a jours le centre de la carte apres le chargement de la position
    var centre = false;
    geolocation.on('change', function() {
        //on centre la carte si c'est pas deja mais on ne la recentre pas ensuite
        if(!centre){
            //TODO
            centre = true;
            const critere = Android.getCritere();
            Android.logFromJs(critere);
            if(critere != undefined) {
                var features = map.getLayers().getArray()[2].getSource().getFeatures();
                var closest = features[0];
                for (var feat in features) {
                    if (containes(feat.getProperties().type, critere)) {
                        if (plusProche(geolocation.getPosition(),feat, closest)) {
                            closest = feat;
                        }
                    }
                }
                features = map.getLayers().getArray()[3].getSource().getFeatures();
                for (feat in features) {
                    if (containes(feat.type, critere)) {
                        if (plusProche(geolocation.getPosition(),feat, closest)) {
                            closest = feat;
                        }
                    }
                }
                map.getView().setCenter(closest.getGeometry().getClosestPoint(geolocation.getPosition()));
                Android.logFromJs(''+closest);
                const overlay = map.getOverlays().getArray()[0];
                content.innerHTML = '<p> nom: </p>';
                overlay.setPosition(closest.getGeometry().getClosestPoint(geolocation.getPosition()));
            }else{
                Android.logFromJs("centrer sur loc");
                map.getView().setCenter(geolocation.getPosition());
            }
        }
    });

    //gere le cercle qui definit la precision
    const accuracyFeature = new ol.Feature();
    geolocation.on('change:accuracyGeometry', function() {
        accuracyFeature.setGeometry(geolocation.getAccuracyGeometry());
    });

    //gere le point de la position
    const positionFeature = new ol.Feature();
    positionFeature.setStyle(new ol.style.Style({
        image: new ol.style.Circle({
            radius: 6,
            fill: new ol.style.Fill({
                color: '#3399CC'
            }),
            stroke: new ol.style.Stroke({
                color: '#fff',
                width: 2
            })
        })
    }));

    //update la position
    geolocation.on('change:position', function() {
        const coordinates = geolocation.getPosition();
        positionFeature.setGeometry(coordinates ?
            new ol.geom.Point(coordinates) : null);
    });

    //ajout de la position sur la carte
    map.addLayer(new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [accuracyFeature, positionFeature]
        })
    }));

    return geolocation;
}