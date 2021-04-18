const React = require('react');
const client = require('../client');

class MegaDLightButton extends React.Component{
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(megadId, port) {
        client({method: 'POST', path: '/api/events/action', entity: {
            mega_d: megadId,
            port: port,
            action: 'SWITCH'
        }});
    }

    render() {
        return (
            <button className="btn btn-primary data-switch" type="button" data-megad={this.props.megaD} data-port={this.props.port}
                    onMouseDown={() => {this.handleClick(this.props.megaD, this.props.port)}}
            >{this.props.title}
            </button>
        )
    }
}

module.exports = MegaDLightButton