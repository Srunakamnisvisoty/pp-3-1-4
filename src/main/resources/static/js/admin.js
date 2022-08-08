$(document).ready(function(){

    $('#newUserLink').click(function(){
        clearNewUser();
    });

    $('#create_user').click(function(){
        addUser();
    });

    $('#clear_user').click(function(){
        clearNewUser();
    });

    refreshUsersTable()
});

function clearNewUser(){
    $('#loginN').val("");
    $('#ageN').val("");
    $('#emailN').val("");
    $('#passwordN').val("");
    $('#securityRolesListN').val([]);
    $('#securityRolesListN option:selected').attr('selected',false);
}

function addUser() {

    let arrSecurityRolesList = [];
    $('#securityRolesListN option:selected').each(function () {
            arrSecurityRolesList.push(JSON.parse($(this).val()));
        });
        let user = {
            "login": $('#loginN').val(),
            "age": $('#ageN').val(),
            "email": $('#emailN').val(),
            "password": $('#passwordN').val(),
            "roleList": $('#roleListN').val().split(","),
            "roles": $('#rolesN').val(),
            "securityRolesList": arrSecurityRolesList

        };
        $.ajax({
            url: 'http://localhost:8080/api/admin/new',
            type: 'post',
            data: JSON.stringify(user),
            headers: {
                'x-auth-token': localStorage.accessToken,
                "Content-Type": "application/json"
            },
            dataType: 'json',
            success: function(){
                refreshUsersTable();
                $('#userTablePage').tab('show');
            },
            error: (data) => showError(data)
        })
}

function openDeleteUserModal(userId) {
    $.get(`http://localhost:8080/api/admin/${userId}`)
        .done((user) => {
            $('#idD').val(user.id);
            $('#loginD').val(user.login);
            $('#ageD').val(user.age);
            $('#emailD').val(user.email);

            $('#securityRolesListD option:selected').attr('selected',false);

            for(let i = 0; i < user.securityRolesList.length; i++){
                $('#securityRolesListD option:contains("' + user.securityRolesList[i].role + '")').attr('selected', true);
            }

            $('#modalDelete').modal('show');


            $('#delete_button').off("click");
            $('#delete_button').click(function () {
                deleteUser(userId);
            })
        });
}

function openEditUserModal(userId) {
    console.log("openEditUserModal " + userId);

    $.ajax({
        url: `http://localhost:8080/api/admin/${userId}`,
        type: 'get',
        headers: {
            'x-auth-token': localStorage.accessToken,
        },
        success: (user) => {
            sendEditRequest(user);
        },
        error: (data) => showError(data)
    })
}

function sendEditRequest(user) {
    const userId = user.id;

    console.log("sendEditRequest " + userId);

    $('#loginE').val(user.login);
    // $('#password').val(user.password);
    $('#idE').val(user.id);
    $('#ageE').val(user.age);
    $('#emailE').val(user.email);
    $('#rolesE').val(user.roles);
    $('#roleListE').val(user.roleList);

    $('#securityRolesListE option:selected').attr('selected',false);

    for(let i = 0; i < user.securityRolesList.length; i++){
        $('#securityRolesListE option:contains("' + user.securityRolesList[i].role + '")').attr('selected', true);
    }

    $('#modalEdit').modal('show');

    $('#edit_button').off("click");

    $('#edit_button').click(() => {
        console.log("edit_button click " + userId);
        let arrSecurityRolesList = [];
        $('#securityRolesListE option:selected').each(function(){
            arrSecurityRolesList.push(JSON.parse($(this).val()));
        });

        let user = {
            "id": $('#idE').val(),
            "login": $('#loginE').val(),
            "age": $('#ageE').val(),
            "email": $('#emailE').val(),
            "password": $('#passwordE').val(),
            "roleList": $('#roleListE').val().split(","),
            "roles": $('#rolesE').val(),
            "securityRolesList": arrSecurityRolesList
        };
        $.ajax({
            url: `http://localhost:8080/api/admin/${userId}`,
            type: 'patch',
            data: JSON.stringify(user),
            headers: {
                'x-auth-token': localStorage.accessToken,
                "Content-Type": "application/json"
            },
            dataType: 'json',
            success: refreshUsersTable,
            error: (data) => showError(data)
        })
    });

}

function refreshUsersTable() {
    $.get(`http://localhost:8080/api/users/me`)
        .done(() => {
            fillInUsersTable(true);
        })
}
function fillInUsersTable(isAdmin) {
    $.get('http://localhost:8080/api/users?t=' + (((new Date()).getTime() * 10000) + 621355968000000000))
        .done((usersList) => {
            $('#modalEdit').modal('hide');
            $('#modalDelete').modal('hide');
            $(document).ready(() => $("#userTablePage").click());
            $(document).ready(() => $("#usersTableBody").empty());
            $(document).ready(() => {
                for (let i = 0; i < usersList.length; i++) {
                    const user = usersList[i];
                    if (isAdmin === true) {
                        $("#usersTableBody")
                            .append($('<tr>')
                                .append($('<td>').text(user.id))
                                .append($('<td>').text(user.login))
                                .append($('<td>').text(user.email))
                                .append($('<td>').text(user.age))
                                .append($('<td>').text(user.roles))
                                .append($('<td>').html("<input onclick=\"openEditUserModal(" + user.id + ")\" type=\"button\" class=\"btn btn-primary\"\n" +
                                    "                                           value=\"Edit\">"))
                                .append($('<td>').html("<input onclick=\"openDeleteUserModal(" + user.id + ")\" type=\"button\" class=\"btn btn-danger\"\n" +
                                    "                                           value=\"Delete\">"))
                            );
                    } else {
                        $("#usersTableBody")
                            .append($('<tr>')
                                .append($('<td>').text(user.id))
                                .append($('<td>').text(user.login))
                                .append($('<td>').text(user.email))
                                .append($('<td>').text(user.age))
                                .append($('<td>').text(user.roles))
                            );
                    }
                }
            });
        });
}


function deleteUser(userId) {
    $.ajax({
        url: 'http://localhost:8080/api/admin/' + userId,
        type: 'delete',
        headers: {
            'x-auth-token': localStorage.accessToken
        },
        success: refreshUsersTable,
        error: (data) => showError(data)
    })
}

function showError(message) {
    alert(message.responseText);
}