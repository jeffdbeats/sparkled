import React from 'react';
import { CompactPicker } from 'react-color';
import './ColorPicker.css';

const SWATCH_COLORS = [
  '#FFF', '#F00', '#0F0', '#00F', '#FF0', '#F0F', '#0FF', '#FC0', '#6FF', '#69F', '#AEA1FF', '#FDA1FF',
  '#CCC', '#C00', '#0C0', '#00C', '#CC0', '#C0C', '#0CC', '#F90', '#3CC', '#36C', '#7B64FF', '#FA28FF',
  '#000', '#900', '#090', '#009', '#990', '#909', '#099', '#F60', '#399', '#039', '#653294', '#AB149E'];

class ColorPicker extends React.Component {
  state = { displayColorPicker: false, color: null };

  constructor(props) {
    super(props);
    this.openColorPicker = this.openColorPicker.bind(this);
    this.closeColorPicker = this.closeColorPicker.bind(this);
    this.changeColor = this.changeColor.bind(this);
  }

  render() {
    const { color, className = '', onDelete } = this.props;

    const colorPicker = !this.state.displayColorPicker ? null : (
      <div className="popover">
        <div className="cover" onClick={this.closeColorPicker}/>
        <CompactPicker colors={SWATCH_COLORS} color={color} onChange={this.changeColor}/>
      </div>
    );

    const deleteButton = onDelete ? <div className="delete-color" onClick={onDelete} title="Delete color">⨯</div> : [];

    return (
      <div className={`ColorPicker ${className}`}>
        <div className="swatch-container">
          <div className="swatch" onClick={this.openColorPicker}>
            <div className="color" style={{ background: color }}/>
            {deleteButton}
          </div>
          {colorPicker}
        </div>
      </div>
    );
  }

  openColorPicker() {
    this.setState({ displayColorPicker: !this.state.displayColorPicker });
  };

  closeColorPicker() {
    if (this.state.color) {
      this.props.onChange(this.state.color);
    }
    this.setState({ displayColorPicker: false });
  };

  changeColor(color) {
    this.setState({ color });
    this.props.onChange(color);
  };
}

export default ColorPicker;

