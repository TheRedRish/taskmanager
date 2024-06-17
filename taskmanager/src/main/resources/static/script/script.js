document.addEventListener("DOMContentLoaded", function () {
    // Add event listener to checkboxes
    document.querySelectorAll('.task-checkbox').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            var taskId = this.getAttribute('data-task-id');
            var completed = this.checked;

            // Send POST request with form data
            fetch('/tasks/updateTaskCompleted', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'id=' + encodeURIComponent(taskId) + '&completed=' + encodeURIComponent(completed)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    // If the response is a redirect, manually handle it
                    if (response.redirected) {
                        window.location.href = response.url;
                    } else {
                        const taskGroupBody = this.closest('.task-group-body');
                        updateProgressBar(taskGroupBody);
                    }
                })
                .catch(error => {
                    console.error('Error updating status:', error);
                });
        });
    });

    function updateProgressBar(taskGroupBody) {
        const checkboxes = taskGroupBody.querySelectorAll('.task-checkbox');
        const totalTasks = checkboxes.length;
        const completedTasks = Array.from(checkboxes).filter(checkbox => checkbox.checked).length;
        const completedPercentage = Math.round((completedTasks / totalTasks) * 100);

        const progressBar = taskGroupBody.querySelector('.group-progress-container progress');
        const progressText = taskGroupBody.querySelector('.group-progress-container span');

        if (progressBar) {
            progressBar.value = completedPercentage;
        }

        if (progressText) {
            progressText.textContent = completedPercentage + '%';
        }
    }

    // Get the popup and the create button
    var popup = document.getElementById("popup");
    var createButton = document.getElementById("createButton");
    var closeButton = document.getElementById("closePopup");
    var createGroupButton = document.getElementById("newGroupButton");

    // Function to show the popup
    function showPopup() {
        popup.style.display = "flex";
    }

    // Function to hide the popup
    function hidePopup() {
        popup.style.display = "none";
    }

    // Event listener for the create button
    createButton.addEventListener("click", function () {
        var groupName = document.getElementById("groupName").value;
        if (groupName) {
            // Send POST request with form data
            fetch('/groups/createGroup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'name=' + encodeURIComponent(groupName)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    // If the response is a redirect, manually handle it
                    if (response.redirected) {
                        window.location.href = response.url;
                    }
                })
                .catch(error => {
                    console.error('Error creating group:', error);
                });
            hidePopup();
        } else {
            alert("Please enter a group name.");
        }
    });

    // Event listener for the create group button
    createGroupButton.addEventListener("click", function () {
        showPopup();
    });

    // Event listener for the close button
    closeButton.addEventListener("click", hidePopup);



    const createTaskButtons = document.querySelectorAll('.create-task-button');
    const deleteButtons = document.querySelectorAll('.delete-button');

    createTaskButtons.forEach(button => {
        button.addEventListener('click', function () {
            const groupId = this.getAttribute('data-group-id');
            document.getElementById('createTaskModal_' + groupId).style.display = 'block';
        });
    });

    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const groupId = this.getAttribute('data-id');
            document.getElementById('deleteModal_' + groupId).style.display = 'block';
        });
    });

    const closeButtons = document.querySelectorAll('.modal .close');
    closeButtons.forEach(button => {
        button.addEventListener('click', function () {
            button.closest('.modal').style.display = 'none';
        });
    });

    window.addEventListener('click', function (event) {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    });
});

$(document).ready(function () {
    $('.task-group-body').after('<div class="spacer"></div>');

    $('.task-row').draggable({
        revert: 'invalid',
        cursor: 'move',
        helper: 'clone'
    });

    $('.task-group-body').droppable({
        accept: '.task-row',
        drop: function (event, ui) {
            var taskId = ui.draggable.find('.task-checkbox').data('task-id');
            var groupId = $(this).data('group-id');

            // Call a function to assign the task to the group in your backend
            assignTaskToGroup(taskId, groupId);

            // Clone the dragged task element
            var newTaskElement = ui.draggable.clone();

            // Remove the cloned helper
            ui.helper.remove();

            // Append the cloned task element to the new group
            $(this).append(newTaskElement);

            // Reinitialize draggable for the newly added task element
            newTaskElement.draggable({
                revert: 'invalid',
                cursor: 'move',
                helper: 'clone'
            });

            // Optionally, you might want to remove the original element if needed
            ui.draggable.remove();
        }
    });
});

function assignTaskToGroup(taskId, groupId) {
    // Send POST request with form data
    fetch('/tasks/' + taskId + '/assign/' + groupId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Task status updated:', data);
        })
        .catch(error => {
            console.error('Error updating task status:', error);
        });
}


