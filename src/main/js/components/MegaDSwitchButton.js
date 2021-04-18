const React = require('react');
const client = require('../client');

class MegaDSwitchButton extends React.Component{
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(megadId, port, event) {
        let input = {pt: port};

        if (event == 'mouseup') {
            input.m = 1;
        }

        client({
            method: 'GET',
            path: `/server/${megadId}`,
            params: input
        });
    }

    render() {
        return (
            <button className="btn btn-primary data-switch" type="button" data-megad={this.props.megaD} data-port={this.props.port}
                    onMouseDown={() => {this.handleClick(this.props.megaD, this.props.port, "mousedown")}}
                    onMouseUp={() => {this.handleClick(this.props.megaD, this.props.port, "mouseup")}}
            >{this.props.title}
            </button>
        )
    }
}

module.exports = MegaDSwitchButton