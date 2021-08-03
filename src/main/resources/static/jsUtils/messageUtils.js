document.write("<script language=javascript src='../js/message.js'></script>");

/**
 *   提示信息框
 * @param type  (info,success,warning,error,loading,destroy)
 * @param message
 * @param timeOut
 */
function getMessage(type, message, timeOut) {
    switch (type) {
        case 'info':
            cocoMessage.config({
                duration: 10000,
            });
            cocoMessage.info(1500, message, function () {
                console.log("close");
            });
            break;
        case 'success':
            cocoMessage.config({
                duration: 10000,
            });
            cocoMessage.success(1500, message, function () {
                console.log("close");
            });
            break;
        case 'warning':
            cocoMessage.config({
                duration: 10000,
            });
            cocoMessage.warning(1500, message, function () {
                console.log("close");
            });
            break;
        case 'error':
            cocoMessage.config({
                duration: 10000,
            });
            cocoMessage.error(1500, message, function () {
                console.log("close");
            });
            break;
        case 'loading':
            cocoMessage.config({
                duration: 10000,
            });
            var closeMsg = cocoMessage.loading(false);
            setTimeout(function () {
                closeMsg();
            }, timeOut);
            break;
        case 'destroy':
            cocoMessage.config({
                duration: 10000,
            });
            cocoMessage.destroyAll();
            break;
    }

}