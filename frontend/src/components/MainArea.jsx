import React from "react";
import "./MainArea.css";
import MainAreaList from "./MainAreaList";

const MainArea = (props) => {
  return (
    <div className="main-area" id="main-area">
      {props.levels.map((level, index) => (
        <MainAreaList
          level={level}
          key={index}
          onClickItem={props.onClickItem}
        />
      ))}
    </div>
  );
};

export default MainArea;
