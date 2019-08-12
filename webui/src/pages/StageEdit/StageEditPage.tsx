import {IconButton, Snackbar, Theme} from "@material-ui/core";
import {Save, Tune} from "@material-ui/icons";
import {makeStyles, useTheme} from "@material-ui/styles";
import produce, {immerable} from "immer";
import React, {useCallback, useEffect, useReducer, useState} from "react";
import {RouteComponentProps} from "react-router-dom";
import ErrorCard from "../../components/ErrorCard";
import PageContainer from "../../components/PageContainer";
import StageEditor from "../../components/StageEditor";
import useTitle from "../../hooks/useTitle";
import {loadStage, saveStage} from "../../rest/StageRestService";
import {StageViewModel} from "../../types/ViewModel";

const useStyles = makeStyles(() => ({
  // The page container never overflows, and the editor tools need to be hidden when they slide offscreen.
  pageContainer: {
    overflow: "hidden"
  },
  container: {
    display: "flex",
    justifyContent: "center",
    width: "100%",
    height: "100%",
    overflow: "hidden"
  }
}));

class StageEditPageState {
  private static readonly [immerable] = true;
  public readonly stage: StageViewModel | null = null;
  public readonly editedStage: StageViewModel | null = null;
  public readonly loading: boolean = false;
  public readonly loadError: [string, string] | null = null;
  public readonly saving: boolean = false;
  public readonly saveError: [string, string] | null = null;
  public readonly toolsVisible: boolean = false;
}

type Action =
  | { type: "Load"; }
  | { type: "LoadSuccess"; payload: StageViewModel }
  | { type: "LoadFailure"; payload: [string, string] | null }
  | { type: "Update"; payload: StageViewModel }
  | { type: "Save"; }
  | { type: "SaveSuccess"; }
  | { type: "SaveFailure"; payload: [string, string] | null }
  | { type: "ToggleTools"; };

const reducer: React.Reducer<StageEditPageState, Action> = (state, action): StageEditPageState => {
  return produce(state, draft => {
    switch (action.type) {
      case "Load":
        draft.loading = true;
        draft.loadError = null;
        break;

      case "LoadSuccess":
        draft.loading = false;
        draft.stage = action.payload;
        break;

      case "LoadFailure":
        draft.loading = false;
        draft.loadError = action.payload;
        break;

      case "Update":
        draft.editedStage = action.payload;
        break;

      case "Save":
        draft.saving = true;
        draft.saveError = null;
        break;

      case "SaveSuccess":
        draft.saving = false;
        break;

      case "SaveFailure":
        draft.saving = false;
        draft.saveError = action.payload;
        break;

      case "ToggleTools":
        draft.toolsVisible = !state.toolsVisible;
        break;
    }
  });
};

type Props = RouteComponentProps<{ stageId: string | undefined }>;

const StageEditPage: React.FC<Props> = props => {
  const theme = useTheme<Theme>();

  const toolsInitiallyVisible = window.outerWidth >= theme.breakpoints.values.sm;
  const [state, dispatch] = useReducer(reducer, {...new StageEditPageState(), toolsVisible: toolsInitiallyVisible});
  const [saved, setSaved] = useState(false);
  const classes = useStyles();

  const toggleTools = useCallback(() => dispatch({type: "ToggleTools"}), []);

  useTitle("Edit Stage");
  useEffect(() => {
    dispatch({type: "Load"});
    loadStage(Number(props.match.params.stageId),
      stage => dispatch({type: "LoadSuccess", payload: stage}),
      error => dispatch({type: "LoadFailure", payload: error}),
    );
  }, [props.match.params.stageId]);

  const updateStage = (stage: StageViewModel) => dispatch({type: "Update", payload: stage});

  let content = <></>;
  if (state.loadError !== null) {
    const [title, body] = state.loadError;
    content = <ErrorCard title={title} body={body} linkUrl="/stages" linkText="Return to stage list"/>;
  } else if (state.stage) {
    const {stage, toolsVisible} = state;
    content = <StageEditor stage={stage} onStageUpdate={updateStage} toolsVisible={toolsVisible} editable={true}/>;
  }

  const save = () => {
    dispatch({type: "Save"});
    saveStage(state.editedStage!,
      () => {
        setSaved(true);
        dispatch({type: "SaveSuccess"});
      },
      error => dispatch({type: "SaveFailure", payload: error})
    );
  };

  const pageBody = <div className={classes.container}>{content}</div>;
  const actions = (
    <>
      <IconButton onClick={save} disabled={state.editedStage === null || state.saving}>
        <Save/>
      </IconButton>
      <IconButton onClick={toggleTools}>
        <Tune/>
      </IconButton>
    </>
  );

  const closeSnackbar = () => setSaved(false);
  return (
    <>
      <PageContainer className={classes.pageContainer} spacing={0} actions={actions}>
        {pageBody}
      </PageContainer>
      <Snackbar
        open={saved}
        anchorOrigin={{vertical: "bottom", horizontal: "right"}}
        message="Stage saved"
        autoHideDuration={3000}
        onClose={closeSnackbar}
      />
    </>
  );
};

export default StageEditPage;
