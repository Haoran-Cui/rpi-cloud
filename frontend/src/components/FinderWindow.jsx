import React, { useState, useEffect } from "react";
import "./MainArea.css";
import SideNavigationBar from "./SideNavigationBar";
import ToolBar from "./ToolBar";
import MainArea from "./MainArea";

const FinderWindow = (props) => {
  // Determine the source of the request
  var req_url = window.location.href;
  var url = "";
  console.log(req_url);

  if (req_url.indexOf("cpolar") !== -1) {
    url = url.substr(0, url.length - 9) + "5725project_war/";
  } else {
    url = "http://192.168.0.107:8080/5725project_war/";
  }
  console.log(url);

  // parameters for fetch
  const url_helper_list = url + "list?path=";
  const url_helper_mkdir = url + "makeDirectory?path=";
  const url_helper_remove = url + "remove?path=";
  const url_helper_upload = url + "upload?path=";
  const url_helper_download = url + "download?path=";
  const metadata = {
    "Content-Type": "application/json-array",
    mode: "cors",
  };

  const [drives, setDrives] = useState([]);
  const [levels, setLevels] = useState([]);
  const [currSelected, setCurrSelected] = useState();
  const [, forceUpdate] = useState(0);

  // initial
  // 1. get all drives connected to rpi
  // 2. get folders and files in first drive
  useEffect(() => {
    fetch(url_helper_list + "/media/pi", metadata)
      .then((res) => res.json())
      .then((fetchedDrives) => {
        setDrives(fetchedDrives);
        forceUpdate((n) => !n); // force update
        fetch(url_helper_list + fetchedDrives[0].path, metadata)
          .then((res) => res.json())
          .then((fetchedLevel) => {
            var newLevels = [];
            newLevels.push(fetchedLevel);
            setLevels(newLevels);
            setCurrSelected(fetchedDrives[0]);
            forceUpdate((n) => !n);
          });
      });
  }, []);

  const handleChangeDrive = (drive) => {
    fetch(url_helper_list + drive.path, metadata)
      .then((res) => res.json())
      .then((fetched) => {
        var newLevels = [];
        newLevels.push(fetched);
        setLevels(newLevels);
        setCurrSelected(drive);
        forceUpdate((n) => !n);
      });
  };

  const handleClickItem = (path) => {
    // get index of file or folder of this path
    var index = [-1, -1];
    for (var i = 0; i < levels.length; i++) {
      for (var j = 0; j < levels[i].length; j++) {
        if (levels[i][j].path === path) {
          index = [i, j];
        }
      }
    }
    // update levels
    var m = index[0];
    var n = index[1];
    if (levels[m][n].selected !== 2) {
      // set curr selected
      setCurrSelected(levels[m][n]);
      // set levels
      var newLevels = [];
      // add up-levels
      for (i = 0; i < m; i++) {
        var level = [];
        for (j = 0; j < levels[i].length; j++) {
          var file = levels[i][j];
          if (file.selected === 2) {
            file.selected = 1;
          }
          level.push(file);
        }
        newLevels.push(level);
      }
      // add curr-level
      var currLevel = levels[m];
      for (i = 0; i < currLevel.length; i++) {
        if (i !== n) {
          currLevel[i].selected = 0;
        } else {
          currLevel[i].selected = 2;
        }
      }
      newLevels.push(currLevel);
      // get and add next level (if necessary)
      if (levels[m][n].type === "folder") {
        var full_url = url_helper_list + levels[m][n].path;
        fetch(full_url, metadata)
          .then((res) => res.json())
          .then((fetchedLevel) => {
            newLevels.push(fetchedLevel);
            setLevels(newLevels);
          });
      } else {
        setLevels(newLevels);
      }
    }
  };

  const handleMakeDirectory = (item) => {
    var path;
    if (item.type === "folder") {
      path = item.path;
    } else {
      var temp = item.path.split("/");
      path = temp.splice(0, temp.length - 1).join("/");
    }
    const full_url = url_helper_mkdir + path;
    const metadata = {
      "Content-Type": "application/json",
      mode: "cors",
    };
    fetch(full_url, metadata)
      .then((res) => res.json())
      .then((result) => {
        helperRefresh(currSelected.path, currSelected.type);
      });
  };

  // helper func: refresh a path
  const helperRefresh = (path, type) => {
    var full_url = url_helper_list + path;
    if (type !== "folder") {
      var temp = path.split("/");
      var fetched_path = temp.splice(0, temp.length - 1).join("/");
      full_url = url_helper_list + fetched_path;
    }
    const metadata = {
      "Content-Type": "application/json-array",
      mode: "cors",
    };
    fetch(full_url, metadata)
      .then((res) => res.json())
      .then((fetchedLevel) => {
        var fetched = fetchedLevel;
        var newLevels = levels;
        if (type !== "folder") {
          for (var i = 0; i < fetched.length; i++) {
            if (fetched[i].path === path) {
              fetched[i].selected = 2;
            }
          }
        }
        newLevels.pop();
        newLevels.push(fetched);
        setLevels(newLevels);
        forceUpdate((n) => !n);
      });
  };

  // handler func: delete one item (folder / file)
  const handleDelete = (itemToBeDeleted) => {
    var path = itemToBeDeleted.path;
    var temp = itemToBeDeleted.path.split("/");
    var up_level_path = temp.splice(0, temp.length - 1).join("/");

    var full_url_for_remove =
      url_helper_remove + path + "&type=" + itemToBeDeleted.type;
    var full_url_for_list = url_helper_list + up_level_path;

    // first fetch for remove
    fetch(full_url_for_remove, metadata).then(() => {
      var up_level_index = helperGetIndex(up_level_path);
      // second fetch for refresh
      fetch(full_url_for_list, metadata)
        .then((res) => res.json())
        .then((result) => {
          var fetched = result;
          var newLevels = [];
          for (var i = 0; i <= up_level_index[0]; i++) {
            newLevels.push(levels[i]);
          }
          newLevels[up_level_index[0]][up_level_index[1]].selected = 2;
          // setCurrSelected(newLevels[up_level_index[0]][up_level_index[1]]);
          newLevels.push(fetched);
          setLevels(newLevels);
          forceUpdate((n) => !n);
        });
    });
  };

  // helper func: get index of a given path
  const helperGetIndex = (path) => {
    for (var i = 0; i < levels.length; i++) {
      for (var j = 0; j < levels[i].length; j++) {
        if (levels[i][j].path === path) {
          return [i, j];
        }
      }
    }
  };

  // helper func: get current active folder
  // 1. currSelected is a folder -> current active folder is this folder
  // 2. currSelected is a file -> current active folder is its up-level folder
  // return active folder's path
  const helperGetActiveFolder = () => {
    if (currSelected.type === "folder") {
      return currSelected.path;
    } else {
      var temp = currSelected.path.split("/");
      var path = temp.splice(0, temp.length - 1).join("/");
      return path;
    }
  };
  // helper func: help refresh after upload
  // * currSelected would not change
  const helperRefresh2 = () => {
    const activeFolderPath = helperGetActiveFolder();
    const full_url = url_helper_list + activeFolderPath;
    const metadata = {
      "Content-Type": "application/json-array",
      mode: "cors",
    };
    const activeFolderIndex = helperGetIndex(activeFolderPath);
    var newLevels = [];
    for (var i = 0; i <= activeFolderIndex[0]; i++) {
      newLevels.push(levels[i]);
    }
    fetch(full_url, metadata)
      .then((res) => res.json())
      .then((result) => {
        if (currSelected.type === "folder") {
          newLevels.push(result);
        } else {
          for (var i = 0; i < result.length; i++) {
            if (result[i].path === currSelected.path) {
              result[i].selected = 2;
            }
          }
          newLevels.push(result);
        }
        setLevels(newLevels);
      });
  };

  const handleUpload = (file) => {
    var formData = new FormData();
    formData.append("filename", file);
    var activeFolderPath = helperGetActiveFolder();
    var full_url = url_helper_upload + activeFolderPath;
    var metadata = {
      "Content-Type": "application/json",
      mode: "cors",
      method: "POST",
      body: formData,
    };
    fetch(full_url, metadata)
      .then((res) => res.json())
      .then((result) => {
        helperRefresh2();
      });
  };

  const handleDownload = () => {
    var full_url = url_helper_download + currSelected.path + "&type=" + currSelected.type;
    console.log(full_url);
    window.open(full_url, "_blank");

  };

  return (
    <div>
      <SideNavigationBar drives={drives} onChangeDrive={handleChangeDrive} />
      <ToolBar
        currSelected={currSelected}
        onMakeDirectory={() => handleMakeDirectory(currSelected)}
        onDelete={() => handleDelete(currSelected)}
        onUpload={handleUpload}
        onDownload={handleDownload}
      />
      <MainArea levels={levels} onClickItem={handleClickItem} />
    </div>
  );
};

export default FinderWindow;
