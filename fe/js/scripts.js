function fetchData() {
    $.ajax({
        url: 'http://localhost:8080/api/users',
        method: 'GET',
        success: function (response) {
            var users = response.data;
            var tableBody = $('#personalDataTableBody');
            tableBody.empty();

            if (users.length === 0) {
                tableBody.append('<tr><td colspan="8" class="text-center">No data found</td></tr>');
            } else {
                users.forEach(function(user, index) {
                    tableBody.append(`
                        <tr>
                            <td>${index + 1}</td> <!-- Nomor urut otomatis -->
                            <td>${user.nik}</td>
                            <td>${user.nama_Lengkap}</td>
                            <td>${user.jenis_Kelamin}</td>
                            <td>${user.tanggal_Lahir}</td>
                            <td>${user.alamat}</td>
                            <td>${user.negara}</td>
                            <td>
                                <button class="btn btn-primary btn-sm" onclick="viewUser(${user.nik})">Detail</button>
                                <button class="btn btn-warning btn-sm" onclick="editUser(${user.nik})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.nik}, '${user.nama_Lengkap}')">Delete</button>
                            </td>
                        </tr>
                    `);
                });
            }
        },
        error: function (error) {
            console.error('Error fetching data:', error);
            alert('Failed to fetch data. Please try again later.');
        }
    });
}

$('#addBtn').click(function () {
    window.location.href = 'add_user.html'; 
});

function viewUser(nik) {
    window.location.href = 'user_detail.html?nik=' + nik;
}

function editUser(nik) {
    window.location.href = 'edit_user.html?nik=' + nik;
}

function deleteUser(nik, namaLengkap) {
    if (confirm(`Anda yakin ingin menghapus data ${namaLengkap}?`)) {
        $.ajax({
            url: 'http://localhost:8080/api/users/' + nik,
            method: 'DELETE',
            success: function () {
                alert('User deleted successfully.');
                fetchData();
            },
            error: function (error) {
                console.error('Error deleting user:', error);
                alert('Failed to delete user. Please try again later.');
            }
        });
    }
}

$(document).ready(function () {
    fetchData();
});
