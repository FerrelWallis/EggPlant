

var columns = [
    [{
        field: "geneid",
        title: "GeneId",
        align: "center",
        halign: "center",
        sortable: true,
        yalign: "middle",
        // formatter: function (value, row) {
        //     return "<a href='/US/SAAS/BGDB/genome/moreInfo?id=" + row.id + "' target='_blank' style='color: cornflowerblue;'>" + row.geneid + "</a>"
        // }
    }, {
        field: "chr",
        title: "Chromesome",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "start",
        title: "Start",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "end",
        title: "End",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "strand",
        title: "Strand",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "kegg",
        title: "KEGG",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true,
    }, {
        field: "pfam",
        title: "Pfam",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "go",
        title: "GO",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true
    }, {
        field: "nr",
        title: "NR",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true,

    }, {
        field: "swissport",
        title: "Swissport",
        align: "center",
        halign: "center",
        yalign: "middle",
        sortable: true,
    }]];

function createTable() {
    var array = ["GeneId", "Chromesome", "Start", "End", "Strand", "KEGG", "Pfam", "GO", "NR", "Swissport"];
    var values = ["geneid", "chr", "start", "end", "strand", "kegg", "pfam", "go", "nr", "swissport"];
    var tHtml = "";

    var html = "";
    $.each(array, function (n, value) {
            html += "<label style='margin-right: 15px'>" +
                "<input type='checkbox' checked='checked' value='" + values[n] + "' onclick=\"setColumns('" + values[n] + "')\">" + value +
                "</label>"
        }
    );
    $("#checkbox").append(html);


}

function hideArray() {
    var hiddenArray = ["kegg", "pfam", "go", "nr", "swissport"];

    $.each(hiddenArray, function (n, value) {
            $('#table').bootstrapTable('hideColumn', value);
            $("input:checkbox[value=" + value + "]").attr("checked", false)
        }
    );
}

function setColumns(value) {
    var element = $("input:checkbox[value=" + value + "]");

    if (element.is(":checked")) {
        $('#table').bootstrapTable('showColumn', value);
    } else {
        $('#table').bootstrapTable('hideColumn', value);
    }
}
