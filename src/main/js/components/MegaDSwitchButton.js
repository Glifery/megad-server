const React = require('react');
const client = require('../client');

class MegaDSwitchButton extends React.Component{
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(event) {
        let input = {pt: this.props.port};

        if (event == 'mouseup') {
            input.m = 1;
        }

        client({
            method: 'GET',
            path: `/server/${this.props.megaD}`,
            params: input
        });
    }

    render() {
        return (
            <button className="btn btn-primary data-switch" type="button" data-megad={this.props.megaD} data-port={this.props.port}
                    onMouseDown={() => {this.handleClick("mousedown")}}
                    onMouseUp={() => {this.handleClick("mouseup")}}
            >{this.props.title}
            </button>
        )
    }
}

module.exports = MegaDSwitchButton