// Document Ready
$(function () {
    inputClearButton(); // Clear Button in Input
    inputShowPassword(); // Show/Hide Password
    fileUploadInit(); // File Upload Setting
    datePicker(); // Date Picker Init
    searchFormCollapse(); // 검색영역 접기/펼치기
    activeBtnToggle(); // 버튼그룹 토글(active) 처리
});

// Clear Button in Input
function inputClearButton() {
    $(".input-group-clear input")
        .on("keydown keyup focus", function (event) {
            $(event.target)
                .parents(".input-group-clear")
                .addClass("input-focus");
            if ($(event.target).val().length > 0) {
                $(event.target).addClass("input-btn-view");
            } else if ($(event.target).val().length === 0) {
                $(event.target).removeClass("input-btn-view");
            }
        })
        .on("blur", function (event) {
            $(event.target)
                .parents(".input-group-clear")
                .removeClass("input-focus");
        });
    $(".input-group-clear .input-btn").on("mousedown", function (event) {
        $(event.target)
            .prevAll("input")
            .val("");
        setTimeout(() => {
            $(event.target)
                .prevAll(".input-btn-view")
                .focus();
        }, 100);
    });
}

// Show/Hide Password in Input
function inputShowPassword() {
    $(".input-group-password input")
        .on("keydown keyup focus", function (event) {
            $(event.target)
                .parents(".input-group-password")
                .addClass("input-focus");
            if ($(event.target).val().length > 0) {
                $(event.target).addClass("input-btn-view");
            } else if ($(event.target).val().length === 0) {
                $(event.target).removeClass("input-btn-view");
            }
        })
        .on("blur", function (event) {
            $(event.target)
                .parents(".input-group-password")
                .removeClass("input-focus");
        });
    $(".input-group-password .input-btn").on("mousedown", function (event) {
        if ($(event.target).hasClass("mdi-eye")) {
            $(event.target)
                .removeClass("mdi-eye")
                .addClass("mdi-eye-off");
            $(event.target)
                .prevAll("input")
                .attr("type", "password");
        } else {
            $(event.target)
                .removeClass("mdi-eye-off")
                .addClass("mdi-eye");
            $(event.target)
                .prevAll("input")
                .attr("type", "text");
        }
        setTimeout(() => {
            $(event.target)
                .prevAll(".input-btn-view")
                .focus();
        }, 100);
    });
}

// File Upload Setting
function fileUploadInit() {
    $(".custom-file-input input").on("change", function () {
        var fileName = $(this)
            .val()
            .split("\\")
            .pop();
        $(this)
            .siblings("span")
            .addClass("selected")
            .html(fileName);
    });
}

// Date Picker
function datePicker() {
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        // startDate: '-3d',
        autoclose: true,
        todayHighlight: true,
        language: "kr"
    });
}

// 검색영역 접기/펼치기
function searchFormCollapse() {
    $(".search-form").append("<div class='close'>Close</div>");
    $(".search-form .close").click(function () {
        if ($(this).parents(".search-form").hasClass("collapsed")) {
            $(this)
                .parents(".search-form")
                .removeClass("collapsed");
        } else {
            $(this)
                .parents(".search-form")
                .addClass("collapsed");
        }
    });
}

// 버튼 active 처리
function activeBtnToggle() {
    $(".active-toggle-group").each(function () {
        $(this)
            .children('.btn')
            .click(function () {
                if ($(this).hasClass('active')) {
                    return;
                } else {
                    $(this)
                        .siblings()
                        .removeClass('active');
                    $(this).addClass('active');
                }
            });
    });
}
