<?php
if (isset($_FILES['myFile'])) {
    // Example:
    if (!is_dir("uploads/")) {
        mkdir("uploads/");
    }

    if (move_uploaded_file($_FILES['myFile']['tmp_name'], "uploads/" . $_FILES['myFile']['name'])) 
    {
        $response["success"] = 1;
        $response["message"] = "Upload successful!";
        die(json_encode($response));
    }
    else
    {
        $response["success"] = 0;
        $response["message"] = "Upload failure!";
        die(json_encode($response));
    }
}
else
{
    $response["success"] = 0;
    $response["message"] = "Upload failure!";
    die(json_encode($response));
}
?>