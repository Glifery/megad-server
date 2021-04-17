const React = require('react');

class Event extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.event.event_date_time}</td>
                <td>{this.props.event.name}</td>
            </tr>
        )
    }
}

module.exports = Event