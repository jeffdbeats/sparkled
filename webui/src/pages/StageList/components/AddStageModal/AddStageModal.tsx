import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Theme,
  useMediaQuery
} from "@material-ui/core";
import {useTheme} from "@material-ui/styles";
import {isEmpty} from "lodash";
import { useSnackbar } from "notistack";
import React, {useCallback, useContext} from "react";
import useForm from "react-hook-form";
import {DataType} from "react-hook-form/dist/types";
import {saveStage} from "../../../../rest/StageRestService";
import {StageViewModel} from "../../../../types/ViewModel";
import {StageListPageDispatchContext, StageListPageStateContext} from "../../StageListPageReducer";

const AddStageModal: React.FC<any> = () => {
  const theme = useTheme<Theme>();
  const fullScreen = useMediaQuery(theme.breakpoints.down("xs"));
  const {register, errors, handleSubmit, formState} = useForm({mode: "onChange"});

  const state = useContext(StageListPageStateContext);
  const dispatch = useContext(StageListPageDispatchContext);
  const snackbar = useSnackbar();

  const addStage = useCallback((data: DataType) => {
    const stage = {...new StageViewModel(), name: data.name};
    dispatch({type: "AddStage", payload: {stage}});
    saveStage(
      stage,
      () => {
        snackbar.enqueueSnackbar("Stage added successfully.", {variant: "success"});
        dispatch({type: "AddStageSuccess"});
      },
      error => {
        snackbar.enqueueSnackbar("Failed to add stage.");
        dispatch({type: "AddStageFailure", payload: {error: error[0]}});
      }
    );
  }, [dispatch, snackbar]);

  const hideAddModal = useCallback(() => {
    dispatch({type: "ToggleAddModal", payload: {visible: false}});
  }, [dispatch]);

  const adding = state.stageToAdd !== null;

  return (
    <Dialog open={state.addModalVisible} onClose={hideAddModal} fullScreen={fullScreen} fullWidth={true}>
      <DialogTitle>Add stage</DialogTitle>
      <DialogContent>
        <form>
          <TextField
            variant="outlined"
            label="Stage name"
            name="name"
            type="text"
            margin="dense"
            InputLabelProps={{shrink: true}}
            disabled={adding}
            error={errors.name !== undefined}
            inputRef={register({required: true})}
          />
        </form>
      </DialogContent>

      <DialogActions>
        <Button onClick={hideAddModal} color="default" disabled={adding}>
          Cancel
        </Button>
        <Button
          variant="contained"
          color="primary"
          type="submit"
          onClick={handleSubmit(addStage)}
          disabled={adding || isEmpty(formState.touched) || !isEmpty(errors)}
        >
          {adding ? "Adding..." : "Add stage"}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AddStageModal;
