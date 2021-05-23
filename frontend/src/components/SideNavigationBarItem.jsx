import React from "react";
import "./SideNavigationBarItem.css";

const SideNavigationBarItem = (props) => {
  const selected = props.selected ? " side-navigation-bar-item-selected" : "";

  return (
    <div className={"side-navigation-bar-item" + selected} onClick={props.handleClick}>
      {props.icon}
      <span className="snb-item-name">{props.name}</span>
    </div>
  );
};

export default SideNavigationBarItem;
