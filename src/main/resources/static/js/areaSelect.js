function loadCity(parentId) {
    var city = $('select[name=city]');
    if (parentId > 0) {
        $.get('/addressDictionary/findAddressDicDataByParent?parent=' + parentId, function (dataResult) {
            var cityHtml = "";
            if (dataResult) {
                for (var i = 0; i < dataResult.length; i++) {
                    var obj = dataResult[i];
                    cityHtml += "<option value='" + obj['id'] + "'>"
                        + obj['name'] + "</option>";
                }

            }
            city.html(cityHtml);
            loadTown(dataResult[0].id)

        })
    }


}
function loadTown(parentId) {
    var town = $('select[name=town]');
    if (parentId > 0) {
        $.get('/addressDictionary/findAddressDicDataByParent?parent=' + parentId, function (dataResult) {
            var townHtml = "";
            if (dataResult) {
                for (var i = 0; i < dataResult.length; i++) {
                    var obj = dataResult[i];
                    townHtml += "<option value='" + obj['id'] + "'>"
                        + obj['name'] + "</option>";
                }

            }
            town.html(townHtml);

        })
    }

}