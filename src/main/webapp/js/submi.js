let tagCheckboxes = document.querySelectorAll(".tag-checkbox");

tagCheckboxes.forEach(function(checkbox) {
  checkbox.addEventListener("change", function() {
    let checkedTags = document.querySelectorAll(".tag-checkbox:checked");
    if (checkedTags.length > 3) {
      this.checked = false;
    }
  });
});
