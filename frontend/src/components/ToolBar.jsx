import React from "react";
import "./ToolBar.css";

import ToolBarButton from "./ToolBarButton";
import ToolBarButtonUpload from "./ToolBarButtonUpload";
import * as icons from "./UtilIcons.jsx";

const ToolBar = (props) => {
  var currActiveFolder = "";
  if (props.currSelected !== undefined) {
    var currActiveFolderPathArray =  props.currSelected.path.split("/");
    if (props.currSelected.type === "folder") {
      currActiveFolder = currActiveFolderPathArray[currActiveFolderPathArray.length - 1];
    } else {
      currActiveFolder = currActiveFolderPathArray[currActiveFolderPathArray.length - 2];
    }
  }
  return (
    <div className="tool-bar">
      <div className="tool-bar-curr-selected-name">{currActiveFolder}</div>
      <div className="tool-bar-btns">
        <ToolBarButton icon={icons.delete2} onClick={props.onDelete} />
        <ToolBarButton
          icon={icons.addfolder3}
          onClick={props.onMakeDirectory}
        />
        <ToolBarButtonUpload onUpload={props.onUpload} />
        <ToolBarButton icon={icons.download1} onClick={props.onDownload} />
      </div>
    </div>
  );
};

export default ToolBar;
