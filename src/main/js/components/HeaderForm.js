const React = require('react');
const client = require('../client');
const MegaDSwitchButton = require('./MegaDSwitchButton')
const MegaDLightButton = require('./MegaDLightButton')

class HeaderForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            ports: {
                input: {},
                output: {}
            },
            portStates: {}
        };
    }

    componentDidMount() {
        setInterval(this.syncPortStates.bind(this), 1000);

        client({method: 'GET', path: '/api/ports/states'})
            .then(response => {
                let ports = {
                    input: {},
                    output: {}
                };

                Object.entries(response.entity).forEach(([name, port]) => {
                    switch (port.port.type) {
                        case 'input':
                            ports.input[name] = <MegaDSwitchButton key={name} megaD={port.port.mega_d.id} port={port.port.number} title={port.port.title} />;
                            break;
                        case 'output':
                            ports.output[name] = <MegaDLightButton key={name} megaD={port.port.mega_d.id} port={port.port.number} title={port.port.title} state={port.state} ref={React.createRef()} />;
                            break;
                    }
                })

                this.setState({
                    ports: ports,
                });
            });
    }

    syncPortStates() {
        client({method: 'GET', path: '/api/ports/states'})
            .done(response => {
                Object.entries(response.entity).forEach(([name, port]) => {
                    if (port.port.type != "output") {
                        return;
                    }
                    if (this.state.ports.output[name].ref.current == null) {
                        return;
                    }
                    this.state.ports.output[name].ref.current.syncPortState(port.state)
                })
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
                { this.state.ports ?
                    <div className="row">
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Эмуляция выключателей</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderSwitchButton('megad1', 0)}
                                        {this.renderSwitchButton('megad1', 1)}
                                        {this.renderSwitchButton('megad1', 2)}
                                        {this.renderSwitchButton('megad2', 18)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Прихожая</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad1', 25)}
                                        {this.renderLightButton('megad1', 7)}
                                        {this.renderLightButton('megad1', 13)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Кухня</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad1', 10)}
                                        {this.renderLightButton('megad1', 8)}
                                        {this.renderLightButton('megad1', 12)}
                                        {this.renderLightButton('megad1', 23)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Гостиная</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad1', 27)}
                                        {this.renderLightButton('megad1', 9)}
                                        {this.renderLightButton('megad1', 22)}
                                        {this.renderLightButton('megad1', 28)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Санузел</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad2', 13)}
                                        {this.renderLightButton('megad2', 7)}
                                        {this.renderLightButton('megad2', 24)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Кабинет</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad2', 10)}
                                        {this.renderLightButton('megad2', 23)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Спальня</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad2', 25)}
                                        {this.renderLightButton('megad2', 9)}
                                        {this.renderLightButton('megad2', 28)}
                                        {this.renderLightButton('megad2', 27)}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">Прочее</h5>
                                    <div className="d-grid gap-2">
                                        {this.renderLightButton('megad2', 22)}
                                        {this.renderLightButton('megad2', 12)}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    : ''}
            </div>
        )
    }
}

module.exports = HeaderForm