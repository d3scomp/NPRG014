/*
Features:
- interoperability with existing JS code

*/

namespace e09_client {
    interface ClickableElement {
        noOfClicks?: number
    }

    jQuery(document).ready(() => {
        jQuery("#click-text").click((event) => {

            let elem = <ClickableElement>event.currentTarget

            if (elem.noOfClicks === undefined) {
                elem.noOfClicks = 1
            } else {
                elem.noOfClicks++
            }

            jQuery(elem).text(`This text has been clicked ${ elem.noOfClicks } times.`)
        })
    })
}