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
            centre = true;
            const critere = Android.getCritere();
            if(critere) {
                const temp = Android.getLocationCritere('' + geolocation.getPosition());
                if(temp) {
                    const point = JSON.parse(temp);
                    const content = document.getElementById('popup-content');//le contenut du pop-up
                    const overlay = map.getOverlays().getArray()[0];
                    const distance = distanceBetween([point.lon, point.lat],geolocation.getPosition());
                    content.innerHTML = '<p> nom: ' + point.name + '</p>'
                        +'<p>coordonée: '+[point.lon, point.lat]+'</p>'
                        +'<p>distance: '+distance+'</p>';
                    overlay.setPosition([point.lon, point.lat]);
                    map.getView().setCenter([point.lon, point.lat]);
                }else{
                    Android.logFromJs("centrer sur loc");
                    map.getView().setCenter(geolocation.getPosition());
                }
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