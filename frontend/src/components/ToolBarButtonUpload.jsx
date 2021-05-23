import React from "react";
import "./ToolBarButtonUpload.css";
import * as icons from "./UtilIcons";

const ToolBarButtonUpload = (props) => {
  // Create a reference to the hidden file input element
  const hiddenFileInput = React.useRef(null);

  // Programatically click the hidden file input element
  // when the Button component is clicked
  const handleClick = (event) => {
    hiddenFileInput.current.click();
  };
  // Call a function (passed as a prop from the parent component)
  // to handle the user-selected file
  const handleChange = (event) => {
    const fileUploaded = event.target.files[0];
    props.onUpload(fileUploaded);
  };
  return (
    <div className={"tool-bar-btn-upload-container"}>
      <button onClick={handleClick} className={"tool-bar-btn-upload"}>
        {icons.upload5}
      </button>
      <input
        type="file"
        ref={hiddenFileInput}
        onChange={handleChange}
        style={{ display: "none" }}
      />
    </div>
  );
};

export default ToolBarButtonUpload;
