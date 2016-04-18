$(document).ready(function() {

  $("#buttonId").click(function() {
    getItems(getItems($('#otsing').val()));
  });

  function getItems(searchWord) {
    jQuery.getJSON('../api/' + searchWord, function(data) {
      var items = [];
      $.each(data, function(key, val) {
        items.push(val);
      });
      $('#city').text(items[0]);
      $('#mean').text(items[1].toFixed(2) + " €");
      $('#std').text(items[2].toFixed(2) + " €");
      $('#mediaan').text(items[3].toFixed(2) + " €");
      console.log(items);
    });
  }
});
