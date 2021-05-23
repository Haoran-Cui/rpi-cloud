import React, { useState } from "react";
import SideNavigationBarItem from "./SideNavigationBarItem";
import "./SideNavigationBar.css";
import * as icons from "./UtilIcons.jsx";

const SideNavigationBar = (props) => {
  const [selectedIndex, setSelectedIndex] = useState(0);

  const handleClick = (index, drive) => {
    setSelectedIndex(index);
    props.onChangeDrive(drive);
  };

  return (
    <div className="side-navigation-bar">
      <div className="snb-favorites">My Drives</div>
      <div className="snb-item-container">
        {props.drives.map((drive, index) => (
          <SideNavigationBarItem
            name={drive.name}
            icon={icons.hdd1}
            key={index}
            index={index}
            selected={selectedIndex === index ? true : false}
            handleClick={() => handleClick(index, drive)}
          />
        ))}
      </div>
    </div>
  );
};

export default SideNavigationBar;
