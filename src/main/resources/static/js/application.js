$(document).ready(function () {
    $('[data-toggle="table"]').bootstrapTable();
});

function currencySorter(a, b) {
    const aStripped = Number(a.replace(/(^\$|,|£)/g, ''));
    const bStripped = Number(b.replace(/(^\$|,|£)/g, ''));
    if (aStripped < bStripped) {
        return -1;
    }
    else if (aStripped > bStripped) {
        return 1;
    }
    return -0;
}