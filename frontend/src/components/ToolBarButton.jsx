import React from "react";
import "./ToolBarButton.css";

const ToolBarButton = (props) => {
  return (
    <div className="tool-bar-btn-n-arrow" onClick={props.onClick}>
      {props.icon}
    </div>
  );
};

export default ToolBarButton;
