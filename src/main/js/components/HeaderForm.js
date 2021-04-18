const React = require('react');
const client = require('../client');
const MegaDSwitchButton = require('./MegaDSwitchButton')

class HeaderForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            buttons: []
        };
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/ports'})
            .done(response => {
                this.setState({
                    buttons: response.entity.map(port =>
                        <MegaDSwitchButton key={port.mega_d.id + port.number} megaD={port.mega_d.id} port={port.number} title={port.title} />
                    )
                })
            });
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title">Прихожая</h5>
                                <div className="d-grid gap-2">
                                    {this.state.buttons}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm">
                        <div className="d-grid gap-2">
                            <button className="btn btn-primary" type="button">Button</button>
                            <button className="btn btn-primary" type="button">Button</button>
                        </div>
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