$(document).ready(function () {
    $('#stats').hide();
    $('#loading').hide();
    $("#buttonId").click(function () {
        getItems(getItems($('#otsing').val()));
    });

    $("#otsing").keyup(function (event) {
    }).keydown(function (event) {
        if (event.which == 13 && $('#otsing').val()) {
            $('#stats').hide();
            getItems(getItems($('#otsing').val()));
            $('#otsing').val('');
        }
    });
    
    $("input").prop('required',true);

    jQuery.validator.setDefaults({
        debug: false,
        success: "valid"
    });

    $("body").validate({
        rules: {
            otsing: "required"
        },
        messages: {
            restaurantId: "Choose restaurant!"
        }
    });
    
    function getItems(searchWord) {
        $('#loading').show();
        var data = {
            city: "Loading...",
            mean: "0",
            std: "0",
            median: "0"
        }
        jQuery.getJSON('../api/' + searchWord, function (data) {
            $.each(data, function (key, val) {
                if (key === "city") {
                    data.city = val;
                }
                if (key === "mean") {
                    data.mean = val;
                }
                if (key === "std") {
                    data.std = val;
                }
                if (key === "median") {
                    data.median = val;
                }
            });
            $('#city').text(data.city);
            $('#mean').text(data.mean.toFixed(2) + " €");
            $('#std').text(data.std.toFixed(2) + " €");
            $('#mediaan').text(data.median.toFixed(2) + " €");
            $('#loading').hide();
            $('#stats').show();
        });
    }

});
