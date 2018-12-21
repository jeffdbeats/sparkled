import BezierEditor from 'bezier-easing-editor';
import _ from 'lodash';
import React from 'react';

class BezierCurveField extends React.Component {

  constructor(props) {
    super(props);
    this.state = { value: this.props.input.value };
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.input.value !== nextProps.input.value) {
      this.setState({ value: nextProps.input.value });
    }
  }

  render() {
    return (
      <BezierEditor value={this.state.value} width={240} height={240} onChange={this.onChange}/>
    );
  }

  onChange = value => {
    // State is updated immediately so the curve is responsive, but the prop update is debounced so we don't pollute
    // the Redux store with updates that blow out the undo stack size.
    this.setState({ value });
    this.updateProps(value);
  }

  updateProps = _.debounce(value => {
    this.props.input.onChange(value);
  }, 100);
}

export default BezierCurveField;

