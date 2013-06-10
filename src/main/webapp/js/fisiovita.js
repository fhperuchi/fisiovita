var mss = mss || {};

mss.mascaras = {
    init: function () {
        "use strict";
        $.mask.options.autoTab = false;
        $.mask.masks.data = {mask: '99/99/9999'};
        $.mask.masks.dataNascimento = {mask: '99/99/9999'};
        $.mask.masks.cep = {mask: '99999-999', placeholder: " "};
        $.mask.masks.cpf = {mask: '999.999.999-99'};
        $.mask.masks.pisPasep = {mask: '999.999.999-99'};
        $.mask.masks.tituloEleitor = {mask: '99999.9999.9999'};
        $.mask.masks.cadastroNacionalDeSaude = {mask: '999.9999.9999.9999'};
        $.mask.masks.declaracaoNascidoVivo = {mask: '999.999.9999-9'};
        $.mask.masks.ric = {mask: '999.999.9999-9'};
        $.mask.masks.tel = {mask: '(99) 999999999'};
        $.mask.masks.numero = {mask: '999999999'};
        $.mask.masks.celular = {mask: '(99) 999999999'};
        $("input:text").setMask();
    },
    disableDrop: function () {
        "use strict";
        $("input[type=text]").each(function () {

            var el = $(this)[0];
            if (el.addEventListener) {

                el.addEventListener("dragover", mss.mascaras.nulla, false);
                el.addEventListener("drop", mss.mascaras.dumpInfo, false);
                // Firefox before version 3.5
                el.addEventListener("dragdrop", mss.mascaras.dumpInfo, false);
                el.addEventListener("ondrop", mss.mascaras.dumpInfo, false);

                el.addEventListener("dragstart", mss.mascaras.nulla, false);
                el.addEventListener("draggesture", mss.mascaras.nulla, false);
                el.addEventListener("drag", mss.mascaras.nulla, false);
                el.addEventListener("dragend", mss.mascaras.nulla, false);
                el.addEventListener("dragenter", mss.mascaras.nulla, false);
                el.addEventListener("dragleave", mss.mascaras.nulla, false);
                el.addEventListener("dragexit", mss.mascaras.nulla, false);

            } else {
                el.attachEvent("ondrop", function () { mss.mascara.dumpInfo(); });
            }

        });
    },
    nulla: function (e) {
        "use strict";
        if (e.type === "dragover") {
            // dragover event needs to be canceled to allow firing the drop
            if (e.preventDefault) {
                e.preventDefault();
            }
        }
    },
    dumpInfo: function (event) {
        "use strict";
        var firedOn = event.target || event.srcElement, uMatch = navigator.userAgent.match(/Firefox\/(.*)$/), ffVersion, limpa = function () {
            $("#" + firedOn.id).val("");
            $("#" + firedOn.id).trigger("change");
            // $("#" + firedOn.id).trigger("blur");
        };
        if (firedOn.tagName === undefined) {
            firedOn = firedOn.parentNode;
        }

        if (uMatch && uMatch.length > 1) {
            ffVersion = uMatch[1];

            if (parseInt(ffVersion, 10) === 3) {
                setTimeout(limpa, 1);
            }
        }

        if (event.preventDefault) {
            event.preventDefault();
        }

        return false;
    }


};