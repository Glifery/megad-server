const React = require('react');
const client = require('../client');
const MegaDSwitchButton = require('./MegaDSwitchButton')
const MegaDLightButton = require('./MegaDLightButton')

class HeaderForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            ports: null
        };
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/ports'})
            .done(response => {
                let ports = {
                    input: {},
                    output: {}
                };

                response.entity.forEach(port => {
                    switch (port.type) {
                        case 'input':
                            ports.input[`${port.mega_d.id}.${port.number}`] = <MegaDSwitchButton key={port.mega_d.id + port.number} megaD={port.mega_d.id} port={port.number} title={port.title} />;
                            break;
                        case 'output':
                            ports.output[`${port.mega_d.id}.${port.number}`] = <MegaDLightButton key={port.mega_d.id + port.number} megaD={port.mega_d.id} port={port.number} title={port.title} />;
                            break;
                    }
                })

                this.setState({
                    ports: ports,
                });
            });
    }

    renderSwitchButton(megaD, port) {
        return this.state.ports.input[`${megaD}.${port}`];
    }

    renderLightButton(megaD, port) {
        return this.state.ports.output[`${megaD}.${port}`];
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm">
                        { this.state.ports ?
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Прихожая</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderSwitchButton('megad1', 0)}
                                        {this.renderSwitchButton('megad1', 1)}
                                        {this.renderSwitchButton('megad1', 2)}
                                    </div>
                                </div>
                            </div>
                        : ''}
                    </div>
                    <div className="col-sm">
                        { this.state.ports ?
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Прихожая</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad1', 24)}
                                        {this.renderLightButton('megad1', 25)}
                                        {this.renderLightButton('megad1', 7)}
                                        {this.renderLightButton('megad1', 13)}
                                    </div>
                                </div>
                            </div>
                        : ''}
                    </div>
                    <div className="col-sm">
                        <div className="d-grid gap-2">
                            <button className="btn btn-primary" type="button">Button</button>
                            <button className="btn btn-primary" type="button">Button</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

module.exports = HeaderForm