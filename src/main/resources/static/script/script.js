function sample() {
    $.ajax({
        cache: false,
        url: "/api/v1/users",
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            url: $('#item').val(),
        }),
        success: function (data) {
            Swal.fire('생성 성공', '생성 되었습니다.', 'success');
        },
        error: function (e) {
            let message = e.responseJSON.message;
            Swal.fire('생성 실패', message, 'error');
        }
    });
}
