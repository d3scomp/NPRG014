var e09_client;
(function (e09_client) {
    jQuery(document).ready(function () {
        jQuery("#click-text").click(function (event) {
            var elem = event.currentTarget;
            if (elem.noOfClicks === undefined) {
                elem.noOfClicks = 1;
            }
            else {
                elem.noOfClicks++;
            }
            jQuery(elem).text("This text has been clicked " + elem.noOfClicks + " times.");
        });
    });
})(e09_client || (e09_client = {}));
//# sourceMappingURL=09-Interop_Client.js.map