import React from "react";
import "./MainAreaListItem.css";
import * as icons from "./UtilIcons.jsx";

const MainAreaListItem = (props) => {
  var divClassName = "main-area-list-item";
  var textClassName = "mali-name";
  if (props.file.selected === 2) {
    divClassName = "main-area-list-item curr-selected";
    textClassName = "mali-name text-curr-selected"
  } else if (props.file.selected === 1) {
    divClassName = "main-area-list-item selected";
  }

  return (
    <div
      className={divClassName}
      onClick={() => props.onClickItem(props.file.path)}
    >
      {props.file.type === "folder" ? icons.folder7 : icons.file3}
      <div className={textClassName}>{props.file.name}</div>
    </div>
  );
};

export default MainAreaListItem;
