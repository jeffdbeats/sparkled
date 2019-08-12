import {Grid, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/styles";
import React, {useCallback, useEffect, useMemo, useReducer, useState} from "react";
import PageContainer from "../../components/PageContainer";
import SearchBar from "../../components/SearchBar";
import SimpleTextCard from "../../components/SimpleTextCard";
import useTitle from "../../hooks/useTitle";
import {loadStages} from "../../rest/StageRestService";
import AddStageModal from "./components/AddStageModal";
import DeleteStageModal from "./components/DeleteStageModal";
import StageCard from "./components/StageCard";
import {
  StageListPageDispatchContext,
  stageListPageReducer,
  StageListPageState,
  StageListPageStateContext
} from "./StageListPageReducer";

const useStyles = makeStyles(() => ({
  root: {
    flexGrow: 1
  }
}));

const StageListPage: React.FC<void> = () => {
  const classes = useStyles();
  const [state, dispatch] = useReducer(stageListPageReducer, new StageListPageState());

  useTitle("Stages");
  useEffect(() => {
    dispatch({type: "LoadStages"});
    loadStages(
      stages => dispatch({type: "LoadStagesSuccess", payload: {stages}}),
      error => dispatch({type: "LoadStagesFailure", payload: {error: error[0]}}),
    );
  }, []);

  const [searchQuery, setSearchQuery] = useState("");
  const filteredStages = useMemo(() => {
    return state.stages.filter(stage => stage.name.toLowerCase().includes(searchQuery.trim().toLowerCase()));
  }, [searchQuery, state.stages]);

  const showAddModal = useCallback(() => {
    dispatch({type: "ToggleAddModal", payload: {visible: true}});
  }, []);

  let bodyContent;
  if (state.loading) {
    bodyContent = <></>;
  } else if (state.loadError) {
    bodyContent = (
      <div className="card border-danger">
        <div className="card-body">
          <Typography variant="body2">{state.loadError}</Typography>
          <button className="btn btn-danger" onClick={window.location.reload}>Reload the page</button>
        </div>
      </div>
    );
  } else if (filteredStages.length === 0) {
    bodyContent = <SimpleTextCard>No stages found.</SimpleTextCard>;
  } else {
    bodyContent = filteredStages.map(stage => (
      <Grid item={true} key={stage.id} xs={12} sm={6} md={4}>
        <StageCard stage={stage}/>
      </Grid>
    ));
  }

  return (
    <StageListPageStateContext.Provider value={state}>
      <StageListPageDispatchContext.Provider value={dispatch}>
        <PageContainer>
          <div className={classes.root}>
            <SearchBar
              placeholderText="Search stages"
              fetching={state.loading}
              onAddButtonClick={showAddModal}
              onSearch={setSearchQuery}
            />

            <Grid container={true} spacing={3}>
              {bodyContent}
            </Grid>

            <AddStageModal/>
            <DeleteStageModal/>
          </div>
        </PageContainer>
      </StageListPageDispatchContext.Provider>
    </StageListPageStateContext.Provider>
  );
};

export default StageListPage;
