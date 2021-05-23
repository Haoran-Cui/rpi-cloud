import React from "react";
import "./MainAreaList.css";
import MainAreaListItem from "./MainAreaListItem";

const MainAreaList = (props) => {
  return (
    <div className="main-area-list">
      {props.level.map((file, index) => {
        return (
          <MainAreaListItem
            file={file}
            key={index}
            onClickItem={props.onClickItem}
          />
        );
      })}
    </div>
  );
};

export default MainAreaList;
