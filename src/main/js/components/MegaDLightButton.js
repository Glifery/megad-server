const React = require('react');
const client = require('../client');

class MegaDLightButton extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            state: props.state
        };
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        client({method: 'POST', path: postActionEndpoint(), entity: {
            mega_d: this.props.megaD,
            port: this.props.port,
            action: 'SWITCH'
        }})
            .then(()=>client({method: 'GET', path: getStateEndpoint(this.props.megaD, this.props.port)}))
            .then(response => {
                this.setState({state: response.entity.state})
            });
    }

    render() {
        return (
            <button
                className={this.state.state == 'ON' ? "btn btn-outline-warning data-switch" : "btn btn-warning data-switch"}
                type="button"
                data-megad={this.props.megaD}
                data-port={this.props.port}
                onMouseDown={this.handleClick}
            >{this.props.title}
            </button>
        )
    }
}

postActionEndpoint = () => '/api/events/action'
getStateEndpoint = (megadId, port) => {
    return '/api/ports/states/' + megadId + '/' + port
}

module.exports = MegaDLightButton