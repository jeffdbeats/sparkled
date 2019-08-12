import produce, {immerable} from "immer";
import {identity} from "lodash";
import React, {createContext, Dispatch} from "react";
import {StageSearchViewModel, StageViewModel} from "../../types/ViewModel";

export class StageListPageState {
  private static [immerable] = true;

  public loading = false;
  public stages: StageSearchViewModel[] = [];
  public loadError = "";

  public addModalVisible = false;
  public stageToAdd: StageViewModel | null = null;
  public addError = "";

  public deleteModalVisible = false;
  public stageIdToDelete: number | null = null;
  public deleteError = "";
}

export type Action =
  | { type: "LoadStages" }
  | { type: "LoadStagesSuccess", payload: { stages: StageSearchViewModel[] } }
  | { type: "LoadStagesFailure", payload: { error: string } }
  | { type: "ToggleAddModal", payload: { visible: boolean } }
  | { type: "AddStage", payload: { stage: StageViewModel } }
  | { type: "AddStageSuccess" }
  | { type: "AddStageFailure", payload: { error: string } }
  | { type: "ToggleDeleteModal", payload: { visible: boolean } }
  | { type: "DeleteStage", payload: { stageId: number } }
  | { type: "DeleteStageSuccess", payload: { stages: StageSearchViewModel[] } }
  | { type: "DeleteStageFailure", payload: { error: string } };

export const StageListPageStateContext = createContext<StageListPageState>(new StageListPageState());
export const StageListPageDispatchContext = createContext<Dispatch<Action>>(identity);

export const stageListPageReducer: React.Reducer<StageListPageState, Action> = (state, action): StageListPageState => {
  return produce(state, draft => {
    switch (action.type) {
      case "LoadStages":
        draft.loading = true;
        draft.loadError = "";
        break;

      case "LoadStagesSuccess":
        draft.loading = false;
        draft.stages = action.payload.stages;
        break;

      case "LoadStagesFailure":
        draft.loading = true;
        draft.loadError = "";
        break;

      case "ToggleAddModal":
        draft.addModalVisible = action.payload.visible;
        break;

      case "AddStage":
        draft.stageToAdd = action.payload.stage;
        draft.addError = "";
        break;

      case "AddStageSuccess":
        draft.stageToAdd = null;
        break;

      case "AddStageFailure":
        draft.stageToAdd = null;
        draft.addError = action.payload.error;
        break;

      case "ToggleDeleteModal":
        draft.deleteModalVisible = action.payload.visible;
        break;

      case "DeleteStage":
        draft.stageIdToDelete = action.payload.stageId;
        draft.deleteError = "";
        break;

      case "DeleteStageSuccess":
        draft.stageIdToDelete = null;
        break;

      case "DeleteStageFailure":
        draft.stageIdToDelete = null;
        draft.deleteError = action.payload.error;
        break;
    }
  });
};
