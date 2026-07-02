import { useEffect, useState } from 'react'
import ClientRow from './ClientRow'

export interface Client {
  id: number
  name: string
  creditScore: number
  loanRate: number
}

function ClientList() {
  const [clients, setClients] = useState<Client[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/clients')
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`)
        }
        return response.json()
      })
      .then((data: Client[]) => setClients(data))
      .catch(() => setError('Could not load clients. Is the backend running?'))
  }, [])

  function handleUpdated(updated: Client) {
    setClients((current) =>
      current.map((client) => (client.id === updated.id ? updated : client)),
    )
  }

  function handleDeleted(id: number) {
    setClients((current) => current.filter((client) => client.id !== id))
  }

  return (
    <>
      {error && <p className="error">{error}</p>}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Credit Score</th>
            <th>Loan Rate</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {clients.map((client) => (
            <ClientRow
              key={client.id}
              client={client}
              onUpdated={handleUpdated}
              onDeleted={handleDeleted}
            />
          ))}
        </tbody>
      </table>
    </>
  )
}

export default ClientList
